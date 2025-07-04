import axios, { AxiosError } from 'axios';
import { Status } from './types'

export async function uploadDataset(file: File, moduleName: string, setStatus: (status: Status) => void) {
  setStatus({
    status: 'uploading',
  });
  console.log("Uploading file");

  const formData = new FormData();
  formData.append('dataset', file);
  formData.append('moduleName', moduleName);

  var id = "";
  try {
    const res = await axios.post("/train/start", formData);
    const { status, content: jobId } = res.data;

    if (status === 'training') {
      console.log(`Training started. Job ID: ${jobId}`);
      id = jobId
    } else {
      console.error('Unexpected training status:', status);
      alert("Unexpected training status");
      throw Error("Unexpected training status");
    }
  } catch (err) {
    const error = err as AxiosError<{ error: string }>;

    console.error('Upload failed:', error.response?.data?.error);
    alert(`Failed to upload dataset: ${error.response?.data?.error ?? 'Unknown error'}`);
    throw new Error("Upload failed");
  }

  setStatus({
    status: 'training',
  });
  pollTrainingStatus(id, setStatus);
  return id;
}

export async function pollTrainingStatus(jobId: string, setStatus: (status: Status) => void) {
  const interval = setInterval(async () => {
    try {
      const res = await axios.get("/train/status", {
        params: { job_id: jobId },
      });

      if (res.data.status === 'completed') {
        clearInterval(interval);
        console.log('Training completed.');
        setStatus({
            status: 'download ready'
        })
      } else {
        console.log(`Job ${jobId} is still in progress...`);
      }
    } catch (err) {
      console.error('Status check failed:', err);
      alert(`Status check failed: ${err}`);
    }
  }, 15000); // Poll every 15 seconds
}

export async function downloadModel(jobId: string) {
  try {
    const res = await axios.get("/model/download", {
      params: { job_id: jobId },
      responseType: 'blob',
    });

    const blob = new Blob([res.data], { type: res.headers['content-type'] });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;

    a.download = `model_${jobId}.task`;

    a.click();
    window.URL.revokeObjectURL(url);
  } catch (err) {
    console.error('Model download failed:', err);
    alert('Failed to download model. Try again later.');
  }
}