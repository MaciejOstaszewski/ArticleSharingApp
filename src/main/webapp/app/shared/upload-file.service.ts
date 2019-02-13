import {Injectable} from '@angular/core';
import {HttpClient, HttpRequest, HttpEvent} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {SERVER_API_URL} from "app/app.constants";
import {map} from "rxjs/operators";
import {IArticle} from "app/shared/model/article.model";

@Injectable()
export class UploadFileService {
    private resourceUrl = SERVER_API_URL + 'api';
    constructor(private http: HttpClient) {}

    pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
        let formData: FormData = new FormData();

        formData.append('file', file);

        const req = new HttpRequest('POST', SERVER_API_URL + 'api/storefile', formData, {
            reportProgress: true,
            responseType: 'text'
        });
        // return this.http.post<any>(this.resourceUrl + '/storefile', formData, { observe: 'response' });
        // return this.http
        //     .post<IArticle>(this.resourceUrl, copy, { observe: 'response' })
        //     .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
        return this.http.request(req);
    }

    getFiles(): Observable<any[]> {
        return this.http.get<any[]>(SERVER_API_URL + 'api/getfiles');

    }

    getImage(image: string): Observable<string> {
        return this.http.get<any>(SERVER_API_URL + 'api/getimage/'+image);
    }

    getImagesWithUrl(images: string[]) : Observable<HttpEvent<{}>> {

        // const req = new HttpRequest('POST', SERVER_API_URL + 'api/renameimages', images, {
        //     reportProgress: true,
        //     responseType: 'text'
        // });
        // return this.http.request(req);
        return this.http.post<string[]>(this.resourceUrl + '/renameimages', images, { observe: 'response' });
    }
}
