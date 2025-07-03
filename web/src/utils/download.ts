import { format } from 'date-fns';

export function downloadFile(file: File) {
  const link = document.createElement('a');
  const timestamp = format(new Date(), 'yyyyMMdd_HHmmss');

  link.href = URL.createObjectURL(file);

  // Use the original file name with timestamp inserted before extension
  const originalName = file.name;
  const dotIndex = originalName.lastIndexOf('.');
  const nameWithoutExt = originalName.slice(0, dotIndex);
  const ext = originalName.slice(dotIndex);
  link.download = `${nameWithoutExt}_${timestamp}${ext}`;

  link.click();
  URL.revokeObjectURL(link.href);
}