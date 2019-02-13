import { Component, Input, OnInit } from '@angular/core';
import { Comment, IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs/index';
import { Account, LoginModalService, Principal } from 'app/core';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from 'app/entities/article';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ICommentWithUser } from 'app/shared/model/comment-with-user.model';

@Component({
    selector: 'jhi-article-details-comment',
    templateUrl: './article-details-comment.component.html',
    styleUrls: ['article-details-comment.scss']
})
export class ArticleDetailsCommentComponent implements OnInit {
    @Input() articleId: number;

    comments: IComment[];
    commentsWithUser: ICommentWithUser[];
    currentAccount: Account;
    eventSubscriber: Subscription;
    modalRef: NgbModalRef;

    private _comment: IComment;
    isSaving: boolean;
    private _commentContent: string;

    constructor(
        private commentService: CommentService,
        private articleService: ArticleService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private loginModalService: LoginModalService
    ) {}

    ngOnInit(): void {
        this.isSaving = false;
        this.commentsWithUser = [];
        this.loadAll();
        this.loadAccount();
        this.registerChangeInComments();
        this.registerAuthenticationSuccess();
    }

    loadAccount() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    loadAll() {
        this.commentService.getAllArticleComments(this.articleId).subscribe(
            (res: HttpResponse<ICommentWithUser[]>) => {
                this.commentsWithUser = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    trackId(index: number, item: IComment) {
        return item.id;
    }

    registerChangeInComments() {
        this.eventSubscriber = this.eventManager.subscribe('commentListModification', response => this.loadAll());
    }

    save() {
        if (this.isUserLogged()) {
            this.isSaving = true;

            this.comment = new Comment();
            this.comment.content = this._commentContent;
            this.comment.articleId = this.articleId;
            if (this.comment.id !== undefined) {
                this.subscribeToSaveResponse(this.commentService.update(this.comment));
            } else {
                this.subscribeToSaveResponse(this.commentService.create(this.comment));
            }
            this.articleService.updateModificationDate(this.comment.articleId).subscribe();
            this.commentContent = '';
        } else {
            this.login();
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>) {
        result.subscribe(
            (res: HttpResponse<IComment>) => {
                this.onSaveSuccess();
                this.loadAll();
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    get commentContent() {
        return this._commentContent;
    }

    set commentContent(value: string) {
        this._commentContent = value;
    }

    get comment() {
        return this._comment;
    }

    set comment(comment: IComment) {
        this._comment = comment;
    }

    quickSend(event) {
        if (event.keyCode === 13) {
            this.save();
        }
    }

    isUserLogged(): boolean {
        return this.currentAccount != null;
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
        });
    }
}
