import { AfterViewInit, Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { HttpErrorResponse, HttpEventType, HttpResponse } from '@angular/common/http';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared';
import { LoginModalService, UserService } from 'app/core';
import { Register } from './register.service';
import { UploadFileService } from 'app/shared/upload-file.service';
import { RandomNameGenerator } from 'app/shared/util/random-name-generator';

@Component({
    selector: 'jhi-register',
    templateUrl: './register.component.html',
    styleUrls: ['register.scss']
})
export class RegisterComponent implements OnInit, AfterViewInit {
    confirmPassword: string;
    doNotMatch: string;
    error: string;
    errorEmailExists: string;
    errorUserExists: string;
    registerAccount: any;
    interests: any[];
    success: boolean;
    modalRef: NgbModalRef;
    imageSrc: string = '';
    avatar: File;
    selectedInterests: number[];

    constructor(
        private languageService: JhiLanguageService,
        private loginModalService: LoginModalService,
        private registerService: Register,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private userService: UserService,
        private uploadService: UploadFileService
    ) {}

    ngOnInit() {
        this.success = false;
        this.registerAccount = {};
        this.interests = [];
        this.userService.interests().subscribe(interests => {
            this.interests = interests;
        });
        this.registerAccount.avatar = '';
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
    }

    register() {
        if (this.registerAccount.password !== this.confirmPassword) {
            this.doNotMatch = 'ERROR';
        } else {
            this.transformToUserInterests();
            this.doNotMatch = null;
            this.error = null;
            this.errorUserExists = null;
            this.errorEmailExists = null;
            this.languageService.getCurrent().then(key => {
                this.registerAccount.langKey = key;
                this.registerService.save(this.registerAccount).subscribe(
                    () => {
                        this.success = true;
                    },
                    response => this.processError(response)
                );
            });
        }
    }

    openLogin() {
        this.modalRef = this.loginModalService.open();
    }

    private processError(response: HttpErrorResponse) {
        this.success = null;
        if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
            this.errorUserExists = 'ERROR';
        } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
            this.errorEmailExists = 'ERROR';
        } else {
            this.error = 'ERROR';
        }
    }

    private readURL(event): void {
        this.avatar = event.target.files.item(0);
        let file = event.target.files.item(0);
        if (file.type.match('image.*')) {
            if (file / 1024 > 10000) {
                alert('image weight too much!, consider change format to JPG');
                return;
            }
        } else {
            alert('invalid format!');
        }

        const reader = new FileReader();
        this.registerAccount.avatar = this.imageSrc;
        reader.onload = e => (this.imageSrc = reader.result);
        reader.readAsDataURL(file);
    }

    upload() {
        if (this.registerAccount.avatar === '') {
            this.register();
        } else {
            const ext = '.' + this.registerAccount.avatar.split('.').pop();
            const filename = RandomNameGenerator.generateRandomName() + ext;
            let newFile = new File([this.avatar], filename, { type: 'image/' + ext });
            this.uploadService.pushFileToStorage(newFile).subscribe(event => {
                if (event.type === HttpEventType.UploadProgress) {
                } else if (event instanceof HttpResponse) {
                    console.log('File is completely uploaded!');
                    this.registerAccount.avatar = filename;
                    this.register();
                }
            });
        }
    }

    private transformToUserInterests() {
        this.registerAccount.interests = [];
        for (let i = 0; i < this.selectedInterests.length; i++) {
            this.registerAccount.interests.push(this.interests[this.selectedInterests[i] - 1]);
        }
    }
}
