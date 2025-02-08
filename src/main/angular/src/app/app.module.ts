import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { SupplierGuard } from './supplier.guard';
import { ConfigurationService } from './api/services/configuration.service';
import { switchMap } from 'rxjs/operators';
import { from } from 'rxjs';
import { ApiModule } from './api/api.module';

function initializeKeycloak(configurationService: ConfigurationService, keycloak: KeycloakService) {
  return () =>
    configurationService.retrieveConfiguration().pipe(
      switchMap(configuration => from(keycloak.init({
        config: {
          url: configuration['application.configuration.frontend.keycloak-url'],
          realm: configuration['application.configuration.frontend.keycloak-realm'],
          clientId: configuration['application.configuration.frontend.keycloak-client-id']
        },
        initOptions: {
          checkLoginIframe: false
        }
      })))
    ).toPromise();
}


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
          provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [HttpClient]
      }
    }),
    ApiModule.forRoot({
      rootUrl: '/genesis-app'
    }),
    NgbModule,
    KeycloakAngularModule
  ],
  bootstrap: [AppComponent],
  providers:[
    SupplierGuard,
    ConfigurationService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [ConfigurationService, KeycloakService],
    }
  ],
})
export class AppModule { }

// required for AOT compilation
export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http, '/genesis-app/app/assets/i18n/', '.json');
}
