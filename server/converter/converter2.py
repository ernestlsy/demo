import mediapipe as mp
from mediapipe.tasks.python.genai import converter

class Converter2:
    def __init__(
        self,
        checkpoint_path,
        tokenizer_path,
        output_path,
    ):
        # Define conversion config
        self.conversion_config = converter.ConversionConfig(
            input_ckpt=checkpoint_path,  # Path to the original Gemma3_1B model checkpoint
            ckpt_format="safetensors",  # Format of the checkpoint
            model_type="GEMMA3_1B",  # Model type
            backend="gpu",  # Backend for inference (gpu or cpu)
            combine_file_only=False,  # Whether to merge files into one binary
            vocab_model_file=tokenizer_path,  # Path to tokenizer vocab file
            output_dir=output_path, # Directory to save the converted outputs
        )

    def run(self):
        # Convert the model to TensorFlow Lite format
        converter.convert_checkpoint(self.conversion_config)