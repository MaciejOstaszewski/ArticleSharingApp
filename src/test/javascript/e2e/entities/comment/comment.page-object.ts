import { element, by, promise, ElementFinder } from 'protractor';

export class CommentComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-comment div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CommentUpdatePage {
    pageTitle = element(by.id('jhi-comment-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    contentInput = element(by.id('field_content'));
    creationDateInput = element(by.id('field_creationDate'));
    modificationDateInput = element(by.id('field_modificationDate'));
    userSelect = element(by.id('field_user'));
    articleSelect = element(by.id('field_article'));

    getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    setContentInput(content): promise.Promise<void> {
        return this.contentInput.sendKeys(content);
    }

    getContentInput() {
        return this.contentInput.getAttribute('value');
    }

    setCreationDateInput(creationDate): promise.Promise<void> {
        return this.creationDateInput.sendKeys(creationDate);
    }

    getCreationDateInput() {
        return this.creationDateInput.getAttribute('value');
    }

    setModificationDateInput(modificationDate): promise.Promise<void> {
        return this.modificationDateInput.sendKeys(modificationDate);
    }

    getModificationDateInput() {
        return this.modificationDateInput.getAttribute('value');
    }

    userSelectLastOption(): promise.Promise<void> {
        return this.userSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    userSelectOption(option): promise.Promise<void> {
        return this.userSelect.sendKeys(option);
    }

    getUserSelect(): ElementFinder {
        return this.userSelect;
    }

    getUserSelectedOption() {
        return this.userSelect.element(by.css('option:checked')).getText();
    }

    articleSelectLastOption(): promise.Promise<void> {
        return this.articleSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    articleSelectOption(option): promise.Promise<void> {
        return this.articleSelect.sendKeys(option);
    }

    getArticleSelect(): ElementFinder {
        return this.articleSelect;
    }

    getArticleSelectedOption() {
        return this.articleSelect.element(by.css('option:checked')).getText();
    }

    save(): promise.Promise<void> {
        return this.saveButton.click();
    }

    cancel(): promise.Promise<void> {
        return this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}
