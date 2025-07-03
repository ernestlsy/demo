from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
import threading
import time
import os
from wrapper import run
from utils.validate_csv import validate_csv

app = Flask(__name__)
CORS(app)
training_jobs = {}

def train_model(job_id, dataset_path):
    model_path = run(dataset_path, job_id)
    training_jobs[job_id] = {"status": "completed", "model_path": model_path}
    return

@app.route("/train", methods=["POST"])
def start_training():
    job_id = str(int(time.time() * 1000))
    dataset = request.files['dataset']
    dataset_path = f"data/{job_id}.csv"
    dataset.save(dataset_path)

    is_valid, message = validate_csv(dataset_path)
    if not is_valid:
        return jsonify({"error": f"Invalid CSV {message}"}), 400
    
    training_jobs[job_id] = {"status": "in_progress", "model_path": None}
    threading.Thread(target=train_model, args=(job_id, dataset_path)).start()

    return jsonify({
        "status": "training",
        "content": job_id
    })

@app.route("/train/status", methods=["GET"])
def check_status():
    job_id = request.args.get("job_id")
    job = training_jobs.get(job_id)
    if not job:
        return jsonify({"error": "Job not found"}), 404
    return jsonify({"status": job["status"]})

@app.route("/train/download", methods=["GET"])
def download_model():
    job_id = request.args.get("job_id")
    job = training_jobs.get(job_id)
    if not job or job["status"] != "completed":
        return jsonify({"error": "Model not ready"}), 400
    return send_file(job["model_path"], as_attachment=True)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)