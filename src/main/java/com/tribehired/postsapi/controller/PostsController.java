package com.tribehired.postsapi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tribehired.postsapi.client.RestClient;
import com.tribehired.postsapi.model.Comment;
import com.tribehired.postsapi.model.PostReq;
import com.tribehired.postsapi.model.PostRes;

@RestController
public class PostsController {

	@Autowired
	RestClient client;

	@Value("${client.url.posts}")
	private String clientUrlPosts;

	@Value("${client.url.comments}")
	private String clientUrlCmts;

	private static ObjectMapper mapper = new ObjectMapper();
	private static Logger log = LoggerFactory.getLogger(PostsController.class);

	@RequestMapping(value = "${api.url.ping}", method = RequestMethod.GET)
	public String ping() {
		return "Welcome!";
	}

	@RequestMapping(value = "${api.url.top.posts}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String findTopPosts() {

		log.info("Start processing findTopPosts ...");
		List<PostReq> topPostLst = null;
		List<Comment> cmtLst = null;
		String result = "";
		try {

			topPostLst = Arrays.asList(mapper.readValue(client.get(clientUrlPosts), PostReq[].class));
			cmtLst = Arrays.asList(mapper.readValue(client.get(clientUrlCmts), Comment[].class));

			for (PostReq post : topPostLst) {

				post.setCommentsCount(
						cmtLst.stream().filter(e -> e.getPostId() == post.getId()).collect(Collectors.toList()).size());
			}

			Collections.sort(topPostLst, Collections.reverseOrder());
			List<PostRes> resultList = topPostLst.stream().map(o -> {
				PostRes r = new PostRes(o.getId(), o.getTitle(), o.getBody(), o.getCommentsCount());
				return r;
			}).collect(Collectors.toList());

			result = mapper.writeValueAsString(resultList);

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "${api.url.posts.filter.comments}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String findComments(@RequestParam(name = "postId", required = false) Integer postId,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "body", required = false) String body) {

		log.info("Start processing findComments ...");
		List<Comment> cmtLst = null;
		List<Comment> resultLst = new ArrayList<Comment>();
		String result = "";
		try {

			List<Predicate<Comment>> pList = new ArrayList<Predicate<Comment>>();
			pList.add((x) -> postId != null && x.getPostId() == postId);
			pList.add((x) -> id != null && x.getId() == id);
			pList.add((x) -> name != null && x.getName().equals(name));
			pList.add((x) -> email != null && x.getEmail().equals(email));
			pList.add((x) -> body != null && x.getBody().contains(body));

			cmtLst = Arrays.asList(mapper.readValue(client.get(clientUrlCmts), Comment[].class));

			for (Predicate<Comment> predicate : pList) {
				log.info("Predicate ...");
				filterComments(cmtLst, predicate)
					.forEach(e -> resultLst.add(e));				
			}

			result = mapper.writeValueAsString(resultLst);

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	private List<Comment> filterComments(List<Comment> comment, Predicate<Comment> predicate) {
		return comment.stream().filter(predicate).collect(Collectors.toList());
	}
}
