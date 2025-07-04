import { useState } from 'react';
import { FileSelect } from './components/FileSelect';
import { FileUpload } from './components/FileUpload';
import { ModelDownload } from './components/ModelDownload';
import { Spinner } from './components/Spinner';
import { Status } from './types';
import { uploadDataset, downloadModel } from './api';
import { FileSpreadsheet, X } from 'lucide-react';

function App() {
  const [status, setStatus] = useState<Status>({
    status: 'idle',
  });
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [fileName, setFileName] = useState<string>("");
  const [jobId, setJobId] = useState<string>("");

  const handleFileSelect = async (file: File) => {
    setSelectedFile(file);
    setFileName(file.name);
    setStatus({
      status: 'file selected',
    });
  };

  const handleUploadFile = async (moduleName: string) => {
    if (selectedFile === null) return;

    try {
      const id = await uploadDataset(selectedFile, moduleName, setStatus); // status is updated within function
      setJobId(id);
    } catch (error) {
      setStatus({
        status: 'file selected',
      });
    }
    return;
  };

  const handleCancelSelection = () => {
    setSelectedFile(null);
    setFileName("");
    setStatus({
      status: 'idle',
    });
  };

  const handleDownload = () => {
    console.log("Downloading model");
    downloadModel(jobId);
  };

  const handleBackToStart = () => {
    setStatus({
      status: 'idle'
    })
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-5xl mx-auto px-4">
        <div className="text-center mb-8">
          <div className="inline-block p-3 bg-blue-100 rounded-full mb-4">
            <FileSpreadsheet className="w-8 h-8 text-blue-600" />
          </div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">
            Dataset Upload for Trainer
          </h1>
          <p className="text-gray-600">
            Upload dataset CSV file to be sent for training model
          </p>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-6 mb-6">
          {status.status === 'idle' && (
            <>
              <FileSelect onFileSelect={handleFileSelect} />
            </>
          )}

          {status.status === 'file selected' && (
            <FileUpload fileName={fileName} onUploadFile={handleUploadFile} onCancel={handleCancelSelection} />
          )}

          {status.status === 'uploading' && (
            <div className="flex items-center justify-center p-6 bg-yellow-50 rounded-lg border border-yellow-200">
              <div className="flex items-center text-yellow-800 font-medium text-lg">
                <Spinner />
                Uploading model...
              </div>
            </div>
          )}

          {status.status === 'training' && (
            <div className="space-y-6">
              <div className="flex items-center justify-center p-6 bg-yellow-50 rounded-lg border border-yellow-200">
                <p className="text-yellow-800 font-medium text-lg">Upload Successful!</p>
              </div>
              <div className="flex items-center justify-center p-6 bg-green-50 rounded-lg border border-green-200">
                <div className="flex items-center text-green-800 font-medium text-lg whitespace-pre-line">
                  <Spinner />
                  Training...
                </div>
              </div>
            </div>
          )}

          {status.status === 'download ready' && (
            <div className="space-y-6">
              <div className="flex items-center justify-center p-6 bg-yellow-50 rounded-lg border border-yellow-200">
                <p className="text-yellow-800 font-medium text-lg">Upload Successful!</p>
              </div>
              <div className="flex items-center justify-center p-6 bg-green-50 rounded-lg border border-green-200">
                <p className="text-green-800 font-medium text-lg whitespace-pre-line">
                  Training Complete!
                </p>
              </div>
              <ModelDownload onDownload={handleDownload} />
              <button
                  onClick={handleBackToStart}
                  className="inline-flex items-center px-6 py-3 bg-red-400 text-white rounded-lg hover:bg-red-500 transition-colors font-medium"
              >
                  <X className="w-5 h-5 mr-2" />
                  Back to start
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;