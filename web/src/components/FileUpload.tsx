import { Play, X, FileText } from 'lucide-react';

interface FileUploadProps {
  fileName: string;
  onUploadFile: () => void;
  onCancel: () => void;
}

export function FileUpload({ fileName, onUploadFile, onCancel }: FileUploadProps) {
    return (
        <div className="space-y-6">
            <div className="flex items-center gap-3 p-4 bg-blue-50 rounded-lg border border-blue-200">
                <FileText className="w-6 h-6 text-blue-600" />
                <div className="flex-1">
                    <h3 className="font-semibold text-blue-900">File Ready for Uploading: {fileName}</h3>
                </div>
            </div>
            <div className="flex gap-3 justify-center">
                <button
                    onClick={onUploadFile}
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