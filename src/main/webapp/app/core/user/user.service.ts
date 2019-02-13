import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUser } from './user.model';
import { IMessage } from 'app/shared/model/message.model';
import { IMessageMerged } from 'app/shared/model/message-merged.model';
import { IInterest } from 'app/shared/model/interest.model';

@Injectable({ providedIn: 'root' })
export class UserService {
    private resourceUrl = SERVER_API_URL + 'api/users';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/users';

    constructor(private http: HttpClient) {}

    create(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.post<IUser>(this.resourceUrl, user, { observe: 'response' });
    }

    update(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.put<IUser>(this.resourceUrl, user, { observe: 'response' });
    }

    find(login: string): Observable<HttpResponse<IUser>> {
        return this.http.get<IUser>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    query(req?: any): Observable<HttpResponse<IUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<IUser[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(login: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
    }

    interests(): Observable<any[]> {
        return this.http.get<any[]>(SERVER_API_URL + 'api/interests');
    }

    getUserInterests(): Observable<IInterest[]> {
        return this.http.get<IInterest[]>(SERVER_API_URL + 'api/users/interests');
    }

    search(req?: any): Observable<HttpResponse<IUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<IUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    getFriends(): Observable<HttpResponse<IUser[]>> {
        return this.http.get<IUser[]>(`${this.resourceUrl}/friends`, { observe: 'response' });
    }

    getStrangers(): Observable<HttpResponse<IUser[]>> {
        return this.http.get<IUser[]>(`${this.resourceUrl}/strangers`, { observe: 'response' });
    }

    invite(id: number): Observable<HttpResponse<any>> {
        return this.http.get(`${this.resourceUrl}/invite/${id}`, { observe: 'response' });
    }

    acceptInvite(id: number): Observable<HttpResponse<any>> {
        return this.http.get(`${this.resourceUrl}/accept/${id}`, { observe: 'response' });
    }

    deleteFromFriends(id: number): Observable<HttpResponse<any>> {
        return this.http.get(`${this.resourceUrl}/delete-from-friends/${id}`, { observe: 'response' });
    }

    loadUserMessages(id: number): Observable<HttpResponse<IMessageMerged[]>> {
        return this.http.get<IMessageMerged[]>(`${this.resourceUrl}/user-messages/${id}`, { observe: 'response' });
    }

    saveUserMessage(message: IMessage): Observable<HttpResponse<IMessage>> {
        console.log(message);
        return this.http.post<IMessage>(this.resourceUrl + '/new-message', message, { observe: 'response' });
    }

    getAllUsers(): Observable<HttpResponse<IUser[]>> {
        return this.http.get<IUser[]>(this.resourceUrl + '/all', { observe: 'response' });
    }
}
