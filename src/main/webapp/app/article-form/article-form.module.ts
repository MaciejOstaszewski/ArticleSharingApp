import { ArticleSharingAppSharedModule } from 'app/shared';
import { ArticleSharingAppAdminModule } from 'app/admin/admin.module';
import { RouterModule } from '@angular/router';
import { ArticleFormComponent } from 'app/article-form/article-form.component';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { articleFormRoute } from 'app/article-form/article-form.route';
import { UploadFileService } from 'app/shared/upload-file.service';

import 'froala-editor/js/froala_editor.pkgd.min.js';
import { QuillModule } from 'ngx-quill';
import { MultiselectDropdownModule } from 'angular-4-dropdown-multiselect';
import { ArticleFromEndComponent } from 'app/article-form/article-from-end.component';

const ENTITY_STATES = [...articleFormRoute];

@NgModule({
    imports: [
        ArticleSharingAppSharedModule,
        ArticleSharingAppAdminModule,
        QuillModule,
        MultiselectDropdownModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [ArticleFormComponent, ArticleFromEndComponent],
    entryComponents: [ArticleFormComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UploadFileService]
})
export class ArticleFormModule {}
