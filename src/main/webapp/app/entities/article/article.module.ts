import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArticleSharingAppSharedModule } from 'app/shared';
import { ArticleSharingAppAdminModule } from 'app/admin/admin.module';
import {
    ArticleComponent,
    ArticleDetailComponent,
    ArticleUpdateComponent,
    ArticleDeletePopupComponent,
    ArticleDeleteDialogComponent,
    articleRoute,
    articlePopupRoute
} from './';

const ENTITY_STATES = [...articleRoute, ...articlePopupRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, ArticleSharingAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ArticleComponent,
        ArticleDetailComponent,
        ArticleUpdateComponent,
        ArticleDeleteDialogComponent,
        ArticleDeletePopupComponent
    ],
    entryComponents: [ArticleComponent, ArticleUpdateComponent, ArticleDeleteDialogComponent, ArticleDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticleSharingAppArticleModule {}
