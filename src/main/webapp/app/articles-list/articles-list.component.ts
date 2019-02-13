import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared';
import { Account, LoginModalService, Principal } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/index';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IArticle } from 'app/shared/model/article.model';
import { ArticleService } from 'app/entities/article/article.service';
import { UploadFileService } from 'app/shared/upload-file.service';
import { Category, ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';

@Component({
    selector: 'jhi-articles-list-component',
    templateUrl: './articles-list.component.html',
    styleUrls: ['articles-list.scss']
})
export class ArticlesListComponent implements OnInit, OnDestroy {
    @Input() active: boolean;

    account: Account;
    modalRef: NgbModalRef;

    showFile = false;
    fileUploads: Observable<string[]>;

    currentAccount: any;
    articles: IArticle[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    categoryId: number;
    tagId: number;

    constructor(
        private uploadService: UploadFileService,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private articleService: ArticleService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private categoryService: CategoryService,
        private router: Router
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    ngOnInit() {
        let params: any = this.activatedRoute.snapshot.params;
        this.categoryId = params.categoryId;
        this.tagId = params.tagId;
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();

        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });

        this.registerChangeInArticles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    loadAll() {
        if (this.currentSearch) {
            this.articleService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IArticle[]>) => this.paginateArticles(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        if (this.categoryId !== undefined) {
            this.articleService
                .getArticleListByCategory(this.active, this.categoryId, {
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IArticle[]>) => this.paginateArticles(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        if (this.tagId !== undefined) {
            this.articleService
                .getArticleListByTag(this.active, this.tagId, {
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IArticle[]>) => this.paginateArticles(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.articleService
            .getArticleList(this.active, {
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IArticle[]>) => this.paginateArticles(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/article'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            "['/']",
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            "['/']",
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    trackId(index: number, item: IArticle) {
        return item.id;
    }

    registerChangeInArticles() {
        this.eventSubscriber = this.eventManager.subscribe('articleListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateArticles(data: IArticle[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.articles = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    addArticle(id: number) {
        this.articleService.activateArticle(id).subscribe();
    }

    deleteArticle(id: number) {
        this.articleService.delete(id).subscribe(() => {
            this.loadAll();
        });
    }

    loadFromCategory(id: any) {
        let waiting = '';
        if (!this.active) waiting = 'waiting';
        this.router.navigate([waiting, 'category', id]);
    }

    loadFromTag(id: any, active: boolean) {
        let waiting = '';
        if (!this.active) waiting = 'waiting';
        this.router.navigate([waiting, 'tag', id]);
    }
}
