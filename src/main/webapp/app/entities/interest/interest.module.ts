import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArticleSharingAppSharedModule } from 'app/shared';
import {
    InterestComponent,
    InterestDetailComponent,
    InterestUpdateComponent,
    InterestDeletePopupComponent,
    InterestDeleteDialogComponent,
    interestRoute,
    interestPopupRoute
} from './';

const ENTITY_STATES = [...interestRoute, ...interestPopupRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        InterestComponent,
        InterestDetailComponent,
        InterestUpdateComponent,
        InterestDeleteDialogComponent,
        InterestDeletePopupComponent
    ],
    entryComponents: [InterestComponent, InterestUpdateComponent, InterestDeleteDialogComponent, InterestDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticleSharingAppInterestModule {}
