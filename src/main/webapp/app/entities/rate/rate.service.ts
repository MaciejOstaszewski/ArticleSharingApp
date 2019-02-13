import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRate } from 'app/shared/model/rate.model';

type EntityResponseType = HttpResponse<IRate>;
type EntityArrayResponseType = HttpResponse<IRate[]>;

@Injectable({ providedIn: 'root' })
export class RateService {
    private resourceUrl = SERVER_API_URL + 'api/rates';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/rates';

    constructor(private http: HttpClient) {}

    create(rate: IRate): Observable<EntityResponseType> {
        return this.http.post<IRate>(this.resourceUrl, rate, { observe: 'response' });
    }

    update(rate: IRate): Observable<EntityResponseType> {
        return this.http.put<IRate>(this.resourceUrl, rate, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRate[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRate[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
