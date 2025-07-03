export interface APIResponse {
  accountType: string;
  primaryClassification: string;
  secondaryClassification: string;
  tertiaryClassification: string;
}

export interface Status {
  status: 'idle' | 'file selected' | 'uploading' | 'training' | 'download ready' | 'error';
  error?: string;
}