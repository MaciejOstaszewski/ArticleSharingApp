import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArticleSharingAppSharedModule } from 'app/shared';
import { ArticleSharingAppAdminModule } from 'app/admin/admin.module';
import {
    MessageComponent,
    MessageDetailComponent,
    MessageUpdateComponent,
    MessageDeletePopupComponent,
    MessageDeleteDialogComponent,
    messageRoute,
    messagePopupRoute
} from './';

const ENTITY_STATES = [...messageRoute, ...messagePopupRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, ArticleSharingAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MessageComponent,
        MessageDetailComponent,
        MessageUpdateComponent,
        MessageDeleteDialogComponent,
        MessageDeletePopupComponent
    ],
    entryComponents: [MessageComponent, MessageUpdateComponent, MessageDeleteDialogComponent, MessageDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticleSharingAppMessageModule {}
