import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpEventType, HttpResponse } from '@angular/common/http';
import { UploadFileService } from 'app/shared/upload-file.service';
import { IUser, UserService } from 'app/core';
import { CategoryService } from 'app/entities/category';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from 'app/entities/article/article.service';
import { InterestService } from 'app/entities/interest';
import { JhiAlertService } from 'ng-jhipster';
import { TagService } from 'app/entities/tag';
import { ICategory } from 'app/shared/model/category.model';
import { ITag } from 'app/shared/model/tag.model';
import { IInterest } from 'app/shared/model/interest.model';
import { IArticle } from 'app/shared/model/article.model';
import { Observable } from 'rxjs/Rx';
import { RandomNameGenerator } from 'app/shared/util/random-name-generator';

@Component({
    selector: 'jhi-article-update',
    templateUrl: './article-form.component.html',
    styleUrls: ['article-form.scss']
})
export class ArticleFormComponent implements OnInit, OnDestroy {
    private _article: IArticle;
    isSaving: boolean;
    users: IUser[];
    categories: ICategory[];
    tags: ITag[];
    interests: IInterest[];
    selectedFiles: FileList;
    currentFileUpload: File;
    images: File[];
    progress: { percentage: number } = { percentage: 0 };
    selectedTags: number[];
    selectedInterests: number[];

    constructor(
        private uploadService: UploadFileService,
        private jhiAlertService: JhiAlertService,
        private articleService: ArticleService,
        private userService: UserService,
        private categoryService: CategoryService,
        private tagService: TagService,
        private interestService: InterestService,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ article }) => {
            this.article = article;
        });

        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.interestService.query().subscribe(
            (res: HttpResponse<IInterest[]>) => {
                this.interests = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.article.images = [];
        this.getSelectedInterests();
        this.getSelectedTags();
    }

    ngOnDestroy(): void {}

    previousState() {
        window.history.back();
    }

    goToEndPage() {
        this.router.navigate(['article/form/end']);
    }

    save() {
        this.isSaving = true;
        this.transformToArticleInterests();
        this.transformToArticleTags();
        this.uploadFiles(this.takeImagesFromContent(this.article.content));
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>) {
        result.subscribe((res: HttpResponse<IArticle>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.goToEndPage();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    trackInterestById(index: number, item: IInterest) {
        return item.id;
    }

    getSelected(selectedValues: Array<any>, option: any) {
        if (selectedValues) {
            for (let i = 0; i < selectedValues.length; i++) {
                if (option.id === selectedValues[i].id) {
                    return selectedValues[i];
                }
            }
        }
        return option;
    }

    get article() {
        return this._article;
    }

    set article(article: IArticle) {
        this._article = article;
    }

    selectFile(event) {
        const file = event.target.files.item(0);

        if (file.type.match('image.*')) {
            this.selectedFiles = event.target.files;
            if (file.size / 1024 > 10000) {
                alert('image weight too much!, consider change format to JPG');
                this.article.imageURL = undefined;
                return;
            }
        } else {
            this.article.imageURL = undefined;
            alert('invalid format!');
        }
    }

    upload() {
        this.progress.percentage = 0;
        this.currentFileUpload = this.selectedFiles.item(0);
        const ext = '.' + this.article.imageURL.split('.').pop();
        const filename = RandomNameGenerator.generateRandomName() + ext;
        let newFile = new File([this.currentFileUpload], filename, { type: 'image/' + ext });
        this.uploadService.pushFileToStorage(newFile).subscribe(event => {
            if (event.type === HttpEventType.UploadProgress) {
                this.progress.percentage = Math.round(100 * event.loaded / event.total);
            } else if (event instanceof HttpResponse) {
                console.log('File is completely uploaded!');
            }
        });

        this.selectedFiles = undefined;
        return filename;
    }

    takeImagesFromContent(content: string) {
        let base64: string = '';
        let images: string[] = [];
        let files: File[] = [];
        for (let i = 0; i < content.length; i++) {
            if (content[i] + content.slice(i + 1, i + 8) == 'img src=') {
                base64 = '';
                for (let j = i + 9; ; j++) {
                    if (content[j] == '"') break;
                    base64 += content[j];
                }

                images.push(base64);
            }
        }

        let type: string = '';
        let b64: string = '';
        for (let i = 0; i < images.length; i++) {
            type = '';
            b64 = '';
            for (let j = 5; j < images[i].length; j++) {
                if (images[i][j] == ';') {
                    b64 += images[i].slice(j + 8, images[i].length);
                    break;
                }
                type += images[i][j];
            }
            let byteCharacters = atob(b64);
            let byteArrays = [];
            let sliceSize = 512;
            for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
                let slice = byteCharacters.slice(offset, offset + sliceSize);

                let byteNumbers = new Array(slice.length);
                for (let i = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }

                let byteArray = new Uint8Array(byteNumbers);
                byteArrays.push(byteArray);
            }
            let fileName = RandomNameGenerator.generateRandomName() + '.jpg';
            this.article.images.push(fileName);
            let blob = new Blob(byteArrays, { type: type });
            files.push(new File([blob], fileName));
        }

        return files;
    }

    changeArticleContentImages() {
        this.article.content = this.article.content.replace(/"(.*?)"/g, '');
        let tmp: string = '';
        let index: number = 0;
        let newContent: string = '';
        for (let i = 0; i < this.article.content.length; i++) {
            tmp += this.article.content[i];
            newContent += this.article.content[i];
            if (tmp.match('src=')) {
                tmp = '';
                newContent += '"' + this.article.images[index] + '"' + ' class="img-responsive article-img" ';
                index++;
            }
        }
        this.article.content = newContent;
    }

    getProperImageNamesFromServer(): any {
        this.uploadService.getImagesWithUrl(this.article.images).subscribe((res: HttpResponse<string[]>) => {
            this.article.images = res.body;
            this.changeArticleContentImages();
            this.saveArticle();
        });
    }

    uploadFiles(files: File[]) {
        this.images = files;
        if (files.length === 0) {
            this.saveArticle();
            return;
        }
        for (let i = 0; i < files.length; i++) {
            this.progress.percentage = 0;
            this.uploadService.pushFileToStorage(files[i]).subscribe(event => {
                if (event.type === HttpEventType.UploadProgress) {
                    this.progress.percentage = Math.round(100 * event.loaded / event.total);
                } else if (event instanceof HttpResponse) {
                    console.log('File is completely uploaded!');
                    setTimeout(() => {
                        this.getProperImageNamesFromServer();
                    }, 1000);
                }
            });
        }
    }

    saveArticle() {
        let filename = this.upload();
        setTimeout(() => {
            this.subscribeToSaveResponse(this.articleService.create(this.article, filename));
        }, 1000);
    }

    private getSelectedTags() {
        if (this.article.tags !== undefined) {
            this.article.tags = [];
            for (let i = 0; i < this.article.tags.length; i++) {
                this.selectedTags.push(this.article.tags[i].id);
            }
        }
    }

    private getSelectedInterests() {
        if (this.article.interests !== undefined) {
            this.article.interests = [];
            for (let i = 0; i < this.article.interests.length; i++) {
                this.selectedInterests.push(this.article.interests[i].id);
            }
        }
    }

    private transformToArticleTags() {
        this.article.tags = [];
        for (let i = 0; i < this.selectedTags.length; i++) {
            this.article.tags.push(this.tags[this.selectedTags[i] - 1]);
        }
    }

    private transformToArticleInterests() {
        this.article.interests = [];
        for (let i = 0; i < this.selectedInterests.length; i++) {
            this.article.interests.push(this.interests[this.selectedInterests[i] - 1]);
        }
    }
}
