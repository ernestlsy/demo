import { Download, FileText } from 'lucide-react';

interface ModelDownloadProps {
  onDownload: () => void;
}

export function ModelDownload({ onDownload }: ModelDownloadProps) {
    return(
        <div className="space-y-6">
            <div className="flex items-center gap-3 p-4 bg-blue-50 rounded-lg border border-blue-200">
            <FileText className="w-6 h-6 text-blue-600" />
            <div className="flex-1">
                <h3 className="font-semibold text-blue-900">Trained Model Ready for Downloading!</h3>
            </div>
            </div>
            <div className="text-center">
            <button
                onClick={onDownload}
                className="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
            >
                <Download className="w-5 h-5 mr-2" />
                Download Trained Model
            </button>
            </div>
        </div>
    )
}