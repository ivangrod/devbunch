package com.devbunch.extractor.crawler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.devbunch.extractor.crawler.dto.PostDTO;
import com.devbunch.extractor.crawler.enums.FieldNameExtractor;
import com.devbunch.extractor.crawler.util.Constant;
import com.devbunch.extractor.crawler.util.JsoupUtil;

public abstract class PostCrawler implements ICrawler {

  protected static String USER_AGENT =
      "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.89 Safari/537.36";

  private static final Logger log = LoggerFactory.getLogger(PostCrawler.class);

  @Override
  public Map<FieldNameExtractor, Object> crawl(final String link) {
    return this.loadFieldPostExtractor(this.extractPostData(link));
  }

  protected PostDTO extractPostData(final String urlPageFrom) {
    PostDTO post = retrievePostDTOByType(urlPageFrom);
    if (post != null) {
      cleanHTML(post);
      disambiguate(post);
    }
    return post;
  }

  /**
   * Clean HTML of all private fields of T PostDTO with @Whitelist basic of @Jsoup elements.
   *
   * @param postDTO
   */
  protected void cleanHTML(final PostDTO postDTO) {
    try {
      BeanInfo bi = Introspector.getBeanInfo(postDTO.getClass());
      final List<PropertyDescriptor> propDes = Arrays.asList(bi.getPropertyDescriptors());
      final Object[] input = null;
      for (PropertyDescriptor prop : propDes) {
        final Method readMethod = prop.getReadMethod();
        if (readMethod != null && readMethod.getReturnType().isAssignableFrom(String.class)) {
          final String value = (String) readMethod.invoke(postDTO, input);
          if (value != null && value.trim().length() > 0) {
            String valueC = Jsoup.clean(value, Whitelist.basic());
            valueC = StringEscapeUtils.unescapeHtml4(valueC);
            final Method writeMethod = prop.getWriteMethod();
            writeMethod.invoke(postDTO, valueC);
          }
        }
      }
    } catch (IntrospectionException | IllegalArgumentException | IllegalAccessException
        | InvocationTargetException e) {

    }
  }

  protected void disambiguate(final PostDTO PostDTO) {

  }

  /**
   * Retrieve the basic version of a post associated to url param.
   *
   * @param urlPageFrom URL page.
   * @param htmlPageCrawl html content from URL
   * @return The basic version of a site.
   */
  protected abstract PostDTO retrievePostDTOByType(final String urlPageFrom);

  public PostDTO retrievePostDTO(String urlPageFrom, final String selectorContent,
      final String selectorCategories) {

    final PostDTO post = new PostDTO();
    try {
      Document doc = JsoupUtil.getJsoupDocument(urlPageFrom, Constant.UTF8_CHARSET, USER_AGENT);
      doc.outputSettings().escapeMode(EscapeMode.xhtml);
      configurePostData(post, doc, selectorContent, selectorCategories);
    } catch (Exception e) {
      log.error("method:{}.{}|cause:\'{}\'|message:\'{}\'|exception:\'{}\'|extra:\'{}\'",
          "AtlassianExtractor", "retrievePostDTO", e.getCause() != null ? e.getCause() : "NULL",
          e.getMessage(), e, urlPageFrom);
    }
    return post;
  }

  protected void configurePostData(final PostDTO post, final Document docHtml,
      final String selectorContent, final String selectorCategories) {

    // 1- CONTENT
    Elements elementsContent = docHtml.select(selectorContent);
    String contentPost =
        (elementsContent != null && !elementsContent.isEmpty()) ? elementsContent.text() : null;
    post.setContent(contentPost);

    // 2- TAGS
    final Set<String> tagsPost = new HashSet<>();
    Elements elementsTags = docHtml.select(selectorCategories);
    elementsTags.forEach(marker -> tagsPost.add(marker.text()));
    post.setTags(tagsPost);
  }

  private Map<FieldNameExtractor, Object> loadFieldPostExtractor(PostDTO post) {
    Map<FieldNameExtractor, Object> fieldAndValuesPost = new HashMap<>();
    if (post != null) {
      fieldAndValuesPost.put(FieldNameExtractor.CONTENT, post.getContent());
      fieldAndValuesPost.put(FieldNameExtractor.TAGS, post.getTags());
    }
    return fieldAndValuesPost;
  }
}
