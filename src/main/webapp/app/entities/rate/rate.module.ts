import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArticleSharingAppSharedModule } from 'app/shared';
import { ArticleSharingAppAdminModule } from 'app/admin/admin.module';
import {
    RateComponent,
    RateDetailComponent,
    RateUpdateComponent,
    RateDeletePopupComponent,
    RateDeleteDialogComponent,
    rateRoute,
    ratePopupRoute
} from './';

const ENTITY_STATES = [...rateRoute, ...ratePopupRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, ArticleSharingAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [RateComponent, RateDetailComponent, RateUpdateComponent, RateDeleteDialogComponent, RateDeletePopupComponent],
    entryComponents: [RateComponent, RateUpdateComponent, RateDeleteDialogComponent, RateDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticleSharingAppRateModule {}
