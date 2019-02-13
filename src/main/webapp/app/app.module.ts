import './vendor.ts';

import { Injector, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { LocalStorageService, Ng2Webstorage, SessionStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { ArticleSharingAppSharedModule } from 'app/shared';
import { ArticleSharingAppCoreModule } from 'app/core';
import { ArticleSharingAppAppRoutingModule } from './app-routing.module';
import { ArticleSharingAppHomeModule } from './home/home.module';
import { ArticleSharingAppAccountModule } from './account/account.module';
import { ArticleSharingAppEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent } from './layouts';
import { ArticleFormModule } from 'app/article-form/article-form.module';
import { FroalaEditorModule, FroalaViewModule } from 'angular-froala-wysiwyg';
import { ArticleDetailsModule } from 'app/article-details/article-details.module';
import { FriendsListModule } from 'app/friends-list/friends-list.module';

import 'froala-editor/js/froala_editor.pkgd.min.js';
import { MessagesModule } from 'app/messages/messages.module';
import { WaitingRoomModule } from 'app/wiating-room/waiting-room.module';
import { ArticlesListModule } from 'app/articles-list/articles-list.module';

@NgModule({
    imports: [
        BrowserModule,
        ArticleSharingAppAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        ArticleSharingAppSharedModule,
        ArticleSharingAppCoreModule,
        ArticleSharingAppHomeModule,
        ArticleSharingAppAccountModule,
        ArticleSharingAppEntityModule,
        ArticleFormModule,
        ArticleDetailsModule,
        FriendsListModule,
        MessagesModule,
        WaitingRoomModule,
        ArticlesListModule,
        FroalaEditorModule.forRoot(),
        FroalaViewModule.forRoot()
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [LocalStorageService, SessionStorageService]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [Injector]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [JhiEventManager]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [Injector]
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class ArticleSharingAppAppModule {}
