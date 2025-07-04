import { Play, X, FileText } from 'lucide-react';
import { useState } from 'react'

interface FileUploadProps {
  fileName: string;
  onUploadFile: (moduleName: string) => void;
  onCancel: () => void;
}

export function FileUpload({ fileName, onUploadFile, onCancel }: FileUploadProps) {
    const [moduleName, setModuleName] = useState("");
    
    const handleUpload = () => {
        if (moduleName.trim() === "") {
        alert("Module name cannot be blank.");
        return;
        }

        onUploadFile(moduleName.trim());
    };
    
    return (
        <div className="space-y-6">
            <div className="flex items-center gap-3 p-4 bg-blue-50 rounded-lg border border-blue-200">
                <FileText className="w-6 h-6 text-blue-600" />
                <div className="flex-1">
                    <h3 className="font-semibold text-blue-900">File Ready for Uploading: {fileName}</h3>
                </div>
            </div>

            <div className="space-y-1">
                <label htmlFor="moduleName" className="font-medium text-sm text-gray-700">
                Module Name
                </label>
                <input
                    id="moduleName"
                    type="text"
                    value={moduleName}
                    onChange={(e) => setModuleName(e.target.value)}
                    className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 ${
                        error ? "border-red-500 focus:ring-red-500" : "border-gray-300 focus:ring-blue-500"
                    }`}
                    placeholder="Enter module name (e.g. incident report)"
                />
            </div>

            <div className="flex gap-3 justify-center">
                <button
                    onClick={handleUpload}
                    className="inline-flex items-center px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors font-medium"
                >
                    <Play className="w-5 h-5 mr-2" />
                    Upload selected file for training
                </button>
                <button
                    onClick={onCancel}
                    className="inline-flex items-center px-6 py-3 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors font-medium"
                >
                    <X className="w-5 h-5 mr-2" />
                    Choose another file
                </button>
            </div>
        </div>
    )
}