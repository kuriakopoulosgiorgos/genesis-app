import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  private onError: Subject<string> = new Subject();
  onError$ = this.onError.asObservable();

  constructor() { }

  error(message: string): void {
    this.onError.next(message);
  }
}
