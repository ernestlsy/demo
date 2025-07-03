from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
import threading
import time
import os
from wrapper import run
from utils.data_utils import validate_csv, validate_feedback, write_to_csv

app = Flask(__name__)
CORS(app)
job_id = 1  # Initialize job_id to 1 at start of server
training_jobs = {}

def train_model(job_id, dataset_path):
    training_jobs[job_id] = {"status": "in_progress", "model_path": None}
    model_path = run(dataset_path, job_id)
    training_jobs[job_id] = {"status": "completed", "model_path": model_path}
    return

@app.route("/train/start", methods=["POST"])
def start_training():
    current_job_id = job_id
    dataset = request.files['dataset']
    dataset_path = f"/data/{current_job_id}.csv"
    dataset.save(dataset_path)

    is_valid, message = validate_csv(dataset_path)
    if not is_valid:
        return jsonify({"error": f"Invalid CSV {message}"}), 400
    
    threading.Thread(target=train_model, args=(current_job_id, dataset_path)).start()

    job_id = job_id + 1
    return jsonify({
        "status": "training",
        "content": current_job_id
    }), 200

@app.route("/train/status", methods=["GET"])
def check_status():
    job_id = request.args.get("job_id")
    job = training_jobs.get(job_id)
    if not job:
        return jsonify({"error": "Job not found"}), 404
    return jsonify({"status": job["status"]}), 200

@app.route("/feedback", methods=["POST"])
def upload_feedback():
    data = request.get_json()
    if not isinstance(data, dict):
        return jsonify({"error": "Expected a JSON object"}), 400
    
    is_valid = validate_feedback(data)
    if not is_valid:
        return jsonify({"error": "Invalid feedback format: module and input_text required."}), 400
    
    ready_for_training, dataset_path = write_to_csv(data)

    if ready_for_training and dataset_path is not None:
        current_job_id = job_id
        threading.Thread(target=train_model, args=(current_job_id, dataset_path)).start()
        job_id = job_id + 1
        return jsonify({"status": "training", "job_id": current_job_id}), 200
    else:
        return jsonify({"status": "stored", "job_id": ""}), 200
    
@app.route("/model/download", methods=["GET"])
def download_model():
    job_id = request.args.get("job_id")
    job = training_jobs.get(job_id)
    if not job or job["status"] != "completed":
        return jsonify({"error": "Model not ready"}), 400
    return send_file(job["model_path"], as_attachment=True)

@app.route("/model/version", methods=["GET"])
def get_newest_version():
    newest_job_id = job_id - 1
    ready_for_download = training_jobs.get(newest_job_id)["status"] == "completed"
    while not ready_for_download:
        newest_job_id = newest_job_id - 1
        if newest_job_id == 0:
            break
        ready_for_download = training_jobs.get(newest_job_id)["status"] == "completed"
    return jsonify({"version": newest_job_id})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)