// ArticlesController.java
package io.collective.articles;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.collective.restsupport.BasicHandler;
import org.eclipse.jetty.server.Request;
import io.collective.restsupport.BasicApp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ArticlesController extends BasicHandler {
    private final ArticleDataGateway gateway;

    public ArticlesController(ObjectMapper mapper, ArticleDataGateway gateway) {
        super(mapper);
        this.gateway = gateway;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (target.equals("/articles")) {
            List<ArticleRecord> articles = gateway.findAll();
            List<ArticleInfo> articleInfos = articles.stream()
                    .map(article -> new ArticleInfo(article.getId(), article.getTitle()))
                    .collect(Collectors.toList());
            writeJsonBody(response, articleInfos);
        } else if (target.equals("/available")) {
            List<ArticleRecord> availableArticles = gateway.findAvailable();
            List<ArticleInfo> availableArticleInfos = availableArticles.stream()
                    .map(article -> new ArticleInfo(article.getId(), article.getTitle()))
                    .collect(Collectors.toList());
            writeJsonBody(response, availableArticleInfos);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        baseRequest.setHandled(true);
    }
}
