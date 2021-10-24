import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'genesis';

  readonly languages = [ 'en', 'el' ];

  readonly languageIcons = {
    "en": "gb",
    "el": "gr"
  }

  currentLanguage: string;

  constructor(private translateService: TranslateService) {
    let language = localStorage.getItem("language");
    // if not previously set
    if(!language) {
    // find the language from the browser settings if it is supported
      language = this.languages.find((l) => l === navigator.language);
    }
    this.currentLanguage = language ? language : 'en';
    localStorage.setItem("language", language);
    this.translateService.setDefaultLang(this.currentLanguage);
  }

  changeLanguage(language: string): void {
    this.currentLanguage = language;
    localStorage.setItem("language", this.currentLanguage);
    this.translateService.setDefaultLang(language);
  }
}
