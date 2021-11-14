import { Component, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CartService } from './cart.service';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { ErrorService } from './error.service';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnDestroy {
  title = 'genesis';

  readonly languages = [ 'en', 'el' ];

  readonly languageIcons = {
    "en": "gb",
    "el": "gr"
  }

  currentLanguage: string;
  itemsInCart = 0;
  error: string;
  isLoggedIn$ = this.keycloakService.isLoggedIn();
  isSupplier = this.keycloakService.getUserRoles().includes('supplier');

  private destroy = new Subject();

  constructor(
    private translateService: TranslateService,
    private cartService: CartService,
    private errorService: ErrorService,
    private keycloakService: KeycloakService) {

    let language = localStorage.getItem("language");
    // if not previously set
    if(!language) {
    // find the language from the browser settings if it is supported
      language = this.languages.find((l) => l === navigator.language);
    }
    this.currentLanguage = language ? language : 'en';
    localStorage.setItem("language", this.currentLanguage);
    this.translateService.setDefaultLang(this.currentLanguage);

    this.cartService.itemsInCart$.pipe(
      takeUntil(this.destroy)
    ).subscribe(itemsInCart => this.itemsInCart = itemsInCart);

    this.errorService.onError$.pipe(
      takeUntil(this.destroy)
    ).subscribe(error => {
      this.error = error;
      setTimeout(() => this.error = undefined, 3000);
    });
  }

  changeLanguage(language: string): void {
    this.currentLanguage = language;
    localStorage.setItem("language", this.currentLanguage);
    this.translateService.setDefaultLang(language);
  }

  async onLogin(): Promise<void> {
    return await this.keycloakService.login({
      redirectUri: window.location.origin
    });
  }

  async onLogout(): Promise<void> {
    return await this.keycloakService.logout(window.location.origin);
  }

  ngOnDestroy(): void {
    this.destroy.next();
    this.destroy.complete();
  }
}
