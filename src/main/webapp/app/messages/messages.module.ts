import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { messagesRoute } from 'app/messages/messages.route';
import { MessagesComponent } from 'app/messages/messages.component';
import { ArticleSharingAppAdminModule } from 'app/admin/admin.module';
import { ArticleSharingAppSharedModule } from 'app/shared';

const ENTITY_STATES = [...messagesRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, ArticleSharingAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [MessagesComponent],
    entryComponents: [MessagesComponent]
})
export class MessagesModule {}
