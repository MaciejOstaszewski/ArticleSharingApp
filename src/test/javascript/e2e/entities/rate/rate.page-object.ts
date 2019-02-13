import { element, by, promise, ElementFinder } from 'protractor';

export class RateComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-rate div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class RateUpdatePage {
    pageTitle = element(by.id('jhi-rate-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    valueInput = element(by.id('field_value'));
    articleSelect = element(by.id('field_article'));
    userSelect = element(by.id('field_user'));

    getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    getValueInput() {
        return this.valueInput;
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
