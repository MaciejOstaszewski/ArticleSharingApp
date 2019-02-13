import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArticleSharingAppSharedModule } from '../../shared';

import {
    ElasticsearchReindexComponent,
    ElasticsearchReindexModalComponent,
    ElasticsearchReindexService,
    elasticsearchReindexRoute
} from './';

const ADMIN_ROUTES = [elasticsearchReindexRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, RouterModule.forChild(ADMIN_ROUTES)],
    declarations: [ElasticsearchReindexComponent, ElasticsearchReindexModalComponent],
    entryComponents: [ElasticsearchReindexModalComponent],
    providers: [ElasticsearchReindexService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticleSharingAppElasticsearchReindexModule {}
