import { Subject } from 'rxjs';
import { map, filter, debounceTime, distinctUntilChanged } from 'rxjs/operators';

export class ProductsGridPresenter {

  private searchTerm: Subject<string> = new Subject();
  searchTerm$ = this.searchTerm.asObservable().pipe(
    map(term => term?.trim()),
    filter(term => term !== undefined),
    debounceTime(600),
    distinctUntilChanged(),
  )

  searchInput = '';

  constructor() {}

  search(): void {
    this.searchTerm.next(this.searchInput);
  }

  reset(): void {
    this.searchInput = '';
    this.searchTerm.next(this.searchInput);
  }
}
