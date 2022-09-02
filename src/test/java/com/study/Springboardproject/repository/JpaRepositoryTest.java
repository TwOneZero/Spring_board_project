package com.study.Springboardproject.repository;

import com.study.Springboardproject.config.JpaConfig;
import com.study.Springboardproject.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@DisplayName("JPA connect-test")
@Import(JpaConfig.class)
@DataJpaTest // -> 각 단위 테스트들은 Transaction 이기에 Rollback 되어서 실제 반영은 안됨
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    //생성자 주입 패턴
    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("Select Test")
    @Test
    void givenTestData_whenSelecting_thenWorksGood() {
        //Given

        //When
        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }
    @DisplayName("Insert Test")
    @Test
    void givenTestData_whenInserting_thenWorksGood() {
        //Given
        long prevCnt = articleRepository.count();
        Article article = Article.of("new one" , "This is new content", "#spring new");
        //When
        articleRepository.save(article);
        //Then
        assertThat(articleRepository.count())
                .isEqualTo(prevCnt + 1);
    }

    @DisplayName("Update Test")
    @Test
    void givenTestData_whenUpdating_thenWorksGood() {
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#for updating";
        article.setHashtag(updatedHashtag);
        //When
        Article updatedArticle =  articleRepository.save(article);
        //Then
        assertThat(updatedArticle)
                .hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("Delete Test")
    @Test
    void givenTestData_whenDeleting_thenWorksGood() {
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long prevArticleCnt = articleRepository.count();
        long prevCommentCnt = articleCommentRepository.count();
        int deleteCommentsSize = article.getArticleComments().size();
        //When
        articleRepository.delete(article);
        //Then
        assertThat(articleRepository.count()).isEqualTo(prevArticleCnt - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(prevCommentCnt - deleteCommentsSize);
    }



}