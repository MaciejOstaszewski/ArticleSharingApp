import { Component, OnInit } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { AccountService, JhiLanguageHelper, Principal, UserService } from 'app/core';
import { IInterest } from 'app/shared/model/interest.model';
import { HttpErrorResponse, HttpEventType, HttpResponse } from '@angular/common/http';
import { RandomNameGenerator } from 'app/shared/util/random-name-generator';
import { UploadFileService } from 'app/shared/upload-file.service';
import { InterestService } from 'app/entities/interest';

@Component({
    selector: 'jhi-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];
    imageSrc: string = '';
    avatar: File;
    avatarURL: string = '';
    private _userInterests: IInterest[];
    selectedInterests: number[];
    interests: IInterest[];

    constructor(
        private account: AccountService,
        private principal: Principal,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private uploadService: UploadFileService,
        private interestService: InterestService
    ) {}

    get userInterests(): IInterest[] {
        return this._userInterests;
    }

    set userInterests(value: IInterest[]) {
        this._userInterests = value;
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.settingsAccount = this.copyAccount(account);
        });
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
        this.userService.getUserInterests().subscribe(interests => {
            this.userInterests = interests;
            this.getSelectedInterests();
        });
        this.interestService.query().subscribe((res: HttpResponse<IInterest[]>) => {
            this.interests = res.body;
        });
    }

    save() {
        this.transformToUserInterests();
        this.account.save(this.settingsAccount).subscribe(
            () => {
                this.error = null;
                this.success = 'OK';
                this.principal.identity(true).then(account => {
                    this.settingsAccount = this.copyAccount(account);
                });
                this.languageService.getCurrent().then(current => {
                    if (this.settingsAccount.langKey !== current) {
                        this.languageService.changeLanguage(this.settingsAccount.langKey);
                    }
                });
            },
            () => {
                this.success = null;
                this.error = 'ERROR';
            }
        );
    }

    copyAccount(account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            interests: this.userInterests,
            avatar: this.avatarURL
        };
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    getAvatar() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
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
        this.settingsAccount.avatar = this.imageSrc;
        reader.onload = e => (this.imageSrc = reader.result);
        reader.readAsDataURL(file);
    }

    upload() {
        if (this.settingsAccount.avatar === '') {
            this.save();
        } else {
            const ext = '.' + this.settingsAccount.avatar.split('.').pop();
            const filename = RandomNameGenerator.generateRandomName() + ext;
            let newFile = new File([this.avatar], filename, { type: 'image/' + ext });
            this.uploadService.pushFileToStorage(newFile).subscribe(event => {
                if (event.type === HttpEventType.UploadProgress) {
                } else if (event instanceof HttpResponse) {
                    console.log('File is completely uploaded!');
                    this.settingsAccount.avatar = filename;
                    this.save();
                }
            });
        }
    }

    deleteImage() {
        this.imageSrc = 'http://placehold.it/180';
        this.settingsAccount.avatar = '';
    }

    private getSelectedInterests() {
        if (this.selectedInterests === undefined) this.selectedInterests = [];
        for (let i = 0; i < this.userInterests.length; i++) {
            this.selectedInterests.push(this.userInterests[i].id);
        }
    }

    private transformToUserInterests() {
        this.settingsAccount.interests = [];
        for (let i = 0; i < this.selectedInterests.length; i++) {
            this.settingsAccount.interests.push(this.userInterests[this.selectedInterests[i] - 1]);
        }
    }
}
