import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IArticle } from 'app/shared/model/article.model';
import { IRate } from 'app/shared/model/rate.model';
import { IAverageRate } from 'app/shared/model/rating-average.model';

type EntityResponseType = HttpResponse<IArticle>;
type EntityArrayResponseType = HttpResponse<IArticle[]>;

@Injectable({ providedIn: 'root' })
export class ArticleService {
    private resourceUrl = SERVER_API_URL + 'api/articles';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/articles';

    constructor(private http: HttpClient) {}

    create(article: IArticle, filename: string): Observable<EntityResponseType> {
        article.imageURL = filename;
        const copy = this.convertDateFromClient(article);
        return this.http
            .post<IArticle>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(article: IArticle): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(article);

        return this.http
            .put<IArticle>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IArticle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IArticle[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getArticleList(active: boolean, req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IArticle[]>(`${this.resourceUrl}/all/${active}`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    updateViews(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/${id}/views`, { observe: 'response' });
    }

    updateModificationDate(id: number): Observable<HttpResponse<any>> {
        return this.http.get(`${this.resourceUrl}/${id}/modification-date`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IArticle[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getUserRating(userLogin: string, articleId: number): Observable<HttpResponse<IRate>> {
        return this.http.get<IRate>(`${this.resourceUrl}/rate/${articleId}/${userLogin}`, { observe: 'response' });
    }

    addRating(rate: IRate): Observable<HttpResponse<any>> {
        return this.http.post<IRate>(this.resourceUrl + '/rate', rate, { observe: 'response' });
    }

    getAverageRating(articleId: number): Observable<HttpResponse<IAverageRate>> {
        return this.http.get<IAverageRate>(`${this.resourceUrl}/rate/average/${articleId}`, { observe: 'response' });
    }

    activateArticle(articleId: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/activate/${articleId}`, { observe: 'response' });
    }

    private convertDateFromClient(article: IArticle): IArticle {
        const copy: IArticle = Object.assign({}, article, {
            creationDate: article.creationDate != null && article.creationDate.isValid() ? article.creationDate.format(DATE_FORMAT) : null,
            modificationDate:
                article.modificationDate != null && article.modificationDate.isValid() ? article.modificationDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
        res.body.modificationDate = res.body.modificationDate != null ? moment(res.body.modificationDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((article: IArticle) => {
            article.creationDate = article.creationDate != null ? moment(article.creationDate) : null;
            article.modificationDate = article.modificationDate != null ? moment(article.modificationDate) : null;
        });
        return res;
    }

    getArticleListByCategory(active: boolean, categoryId: number, req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IArticle[]>(`${this.resourceUrl}/category/${categoryId}/${active}`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getArticleListByTag(active: boolean, tagId: number, req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IArticle[]>(`${this.resourceUrl}/tag/${tagId}/${active}`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getSuggestedArticles(id: number): Observable<HttpResponse<IArticle[]>> {
        return this.http.get<IArticle[]>(`${this.resourceUrl}/suggested/${id}`, { observe: 'response' });
    }
}
