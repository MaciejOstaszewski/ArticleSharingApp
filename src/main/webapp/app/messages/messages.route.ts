import { Routes } from '@angular/router';
import { MessagesComponent } from 'app/messages/messages.component';
import { UserRouteAccessService } from 'app/core';

export class MessagesRoute {}
export const messagesRoute: Routes = [
    {
        path: 'user/messages',
        component: MessagesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articleSharingApp.messages.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
