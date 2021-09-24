export class Utils {

  static omitFileName(path: string): string {
    if(path.includes('/')) {
      return path.split('/').slice(0, -1).join('/');
    }
    return path;
  }
}
