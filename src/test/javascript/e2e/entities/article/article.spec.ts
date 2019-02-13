import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { ArticleComponentsPage, ArticleUpdatePage } from './article.page-object';

describe('Article e2e test', () => {
    let navBarPage: NavBarPage;
    let articleUpdatePage: ArticleUpdatePage;
    let articleComponentsPage: ArticleComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Articles', () => {
        navBarPage.goToEntity('article');
        articleComponentsPage = new ArticleComponentsPage();
        expect(articleComponentsPage.getTitle()).toMatch(/articleSharingApp.article.home.title/);
    });

    it('should load create Article page', () => {
        articleComponentsPage.clickOnCreateButton();
        articleUpdatePage = new ArticleUpdatePage();
        expect(articleUpdatePage.getPageTitle()).toMatch(/articleSharingApp.article.home.createOrEditLabel/);
        articleUpdatePage.cancel();
    });

    it('should create and save Articles', () => {
        articleComponentsPage.clickOnCreateButton();
        articleUpdatePage.setTitleInput('title');
        expect(articleUpdatePage.getTitleInput()).toMatch('title');
        articleUpdatePage.setCreationDateInput('2000-12-31');
        expect(articleUpdatePage.getCreationDateInput()).toMatch('2000-12-31');
        articleUpdatePage.setModificationDateInput('2000-12-31');
        expect(articleUpdatePage.getModificationDateInput()).toMatch('2000-12-31');
        articleUpdatePage.setContentInput('content');
        expect(articleUpdatePage.getContentInput()).toMatch('content');
        articleUpdatePage.setImageURLInput('imageURL');
        expect(articleUpdatePage.getImageURLInput()).toMatch('imageURL');
        articleUpdatePage.setViewsInput('5');
        expect(articleUpdatePage.getViewsInput()).toMatch('5');
        articleUpdatePage.userSelectLastOption();
        articleUpdatePage.categorySelectLastOption();
        articleUpdatePage.tagSelectLastOption();
        articleUpdatePage.interestSelectLastOption();
        articleUpdatePage.save();
        expect(articleUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
