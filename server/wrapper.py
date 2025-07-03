import os
import subprocess
import yaml
import shutil
from trainer.train import InitialTrainer, DefaultTrainer
from bundler.bundler import Bundler

def run(dataset_path, job_id):
    ## Load config
    with open("config.yaml") as f:
        config = yaml.load(f, Loader=yaml.FullLoader)
    trainer_cfg = config["trainer"]
    convert_cfg = config["convert_gemma3_to_tflite"]
    bundler_cfg = config["bundler"]

    ## Step 0: Clear temp folders
    epoch_dir = trainer_cfg["epoch_path"]
    if os.path.exists(epoch_dir):
        print(f"Clearing contents of epoch directory: {epoch_dir}")
        for filename in os.listdir(epoch_dir):
            file_path = os.path.join(epoch_dir, filename)
            try:
                if os.path.isfile(file_path) or os.path.islink(file_path):
                    os.unlink(file_path)
                elif os.path.isdir(file_path):
                    shutil.rmtree(file_path)
            except Exception as e:
                print(f"Failed to delete {file_path}: {e}")
    else:
        print(f"Directory does not exist, creating: {epoch_dir}")
        os.makedirs(epoch_dir)

    merged_dir = trainer_cfg["output_path"]
    if os.path.exists(merged_dir):
        print(f"Clearing contents of merged directory: {merged_dir}")
        for filename in os.listdir(merged_dir):
            file_path = os.path.join(merged_dir, filename)
            try:
                if os.path.isfile(file_path) or os.path.islink(file_path):
                    os.unlink(file_path)
                elif os.path.isdir(file_path):
                    shutil.rmtree(file_path)
            except Exception as e:
                print(f"Failed to delete {file_path}: {e}")
    else:
        print(f"Directory does not exist, creating: {merged_dir}")
        os.makedirs(merged_dir)

    tflite_dir = convert_cfg["output_path"]
    if os.path.exists(tflite_dir):
        print(f"Clearing contents of tflite directory: {tflite_dir}")
        for filename in os.listdir(tflite_dir):
            file_path = os.path.join(tflite_dir, filename)
            try:
                if os.path.isfile(file_path) or os.path.islink(file_path):
                    os.unlink(file_path)
                elif os.path.isdir(file_path):
                    shutil.rmtree(file_path)
            except Exception as e:
                print(f"Failed to delete {file_path}: {e}")
    else:
        print(f"Directory does not exist, creating: {tflite_dir}")
        os.makedirs(tflite_dir)


    ## Step 1: Run trainer

    print("Init trainer...")
    # Check if LoRA adapters already exist
    adapter_path = trainer_cfg["lora_path"] + "/adapter_config.json"
    if os.path.exists(adapter_path):
        trainer = DefaultTrainer(**trainer_cfg, dataset_path=dataset_path)
        print("DefaultTrainer created")
    else:
        trainer = InitialTrainer(**trainer_cfg, dataset_path=dataset_path)
        print("InitalTrainer created")

    print("Running trainer...")
    trainer.train_and_save()

    if os.path.exists(dataset_path):
        print(f"Removing used dataset: {dataset_path}")
        try:
            os.unlink(dataset_path)
        except Exception as e:
            print(f"Failed to delete {dataset_path}: {e}")


    ## Step 2: Run converter with flags
    print("Running converter...")

    converter_cmd = ["python", "converter/convert_gemma3_to_tflite.py"]
    for key, value in convert_cfg.items():
        if isinstance(value, bool):
            value = str(value)
        converter_cmd.append(f"--{key}={value}")

    subprocess.run(converter_cmd, check=True)


    ## Step 3: Run bundler
    print("Init bundler...")
    output_model_name = f"model_{str(job_id)}"
    bundler = Bundler(**bundler_cfg, model_name=output_model_name)

    print("Running bundler...")
    output_path = bundler.create()

    outdated_model_path  = f"{bundler_cfg["output_dir"]}/model_{str(job_id-1)}.task"
    if os.path.exists(outdated_model_path):
        print(f"Removing outdated model: {outdated_model_path}")
        try:
            os.unlink(outdated_model_path)
        except Exception as e:
            print(f"Failed to delete {outdated_model_path}: {e}")

    print("All steps completed.")
    return output_path