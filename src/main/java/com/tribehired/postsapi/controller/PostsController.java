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

/**
 * 
 * @author dil.nawaz
 *
 */
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

	/**
	 * Test connection.
	 * @return
	 */
	@RequestMapping(value = "${api.url.ping}", method = RequestMethod.GET)
	public String ping() {
		return "Welcome!";
	}

	/**
	 * Get posts from endpoint and sort results as required.
	 * @return
	 */
	@RequestMapping(value = "${api.url.top.posts}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String findTopPosts() {

		log.info("Start processing findTopPosts ...");
		List<PostReq> topPostLst = null;
		List<Comment> cmtLst = null;
		String result = "";
		try {

			// 1. Get posts and comments from endpoints.
			topPostLst = Arrays.asList(mapper.readValue(client.get(clientUrlPosts), PostReq[].class));
			cmtLst = Arrays.asList(mapper.readValue(client.get(clientUrlCmts), Comment[].class));

			// 2. Iterate on posts list and update comment total count into post object in the list.
			for (PostReq post : topPostLst) {

				post.setCommentsCount(
						cmtLst.stream().filter(e -> e.getPostId() == post.getId()).collect(Collectors.toList()).size());
			}

			// 3. Sort in descending order based on comments total count.
			Collections.sort(topPostLst, Collections.reverseOrder());
			
			// 4. Copy to result model class to achieve required standard JSON output
			List<PostRes> resultList = topPostLst.stream().map(o -> 
				 new PostRes(o.getId(), o.getTitle(), o.getBody(), o.getCommentsCount())
			).collect(Collectors.toList());

			// 5. Convert list to JSON string.
			result = mapper.writeValueAsString(resultList); // git comment r1.0

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {

		// modify lines added in 1.0 and 2.0 -> r2.0	Added main method in Controller .

		// new line modify lines added in 1.0 and 2.0
	}

	private void dosome(){
		//modify lines added in 1.0 and 2.0
		String str = "Name";
		System.out.println(str);


		// change in connect-globalmaster. r2.0	Added main method in Controller
		String s;
		int abc;

	}

	private void dosomething(){

	};
	/**
	 * Get comments from the endpoint.
	 * @param postId
	 * @param id
	 * @param name
	 * @param email
	 * @param body
	 * @return
	 */
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

			// 1. Initialize the predicate to be used for filtering data from the comments list.
			List<Predicate<Comment>> pList = new ArrayList<Predicate<Comment>>();
			pList.add((x) -> postId != null && x.getPostId() == postId);
			pList.add((x) -> id != null && x.getId() == id);
			pList.add((x) -> name != null && x.getName().equals(name));
			pList.add((x) -> email != null && x.getEmail().equals(email));
			pList.add((x) -> body != null && x.getBody().contains(body));

			// 2. Get comments list from the endpoint.
			cmtLst = Arrays.asList(mapper.readValue(client.get(clientUrlCmts), Comment[].class));

			// 3. Apply all the predicate and copy filtered objects to the result list. (Data Filtering)
			for (Predicate<Comment> predicate : pList) {
				filterComments(cmtLst, predicate)
					.forEach(e -> resultLst.add(e));				
			}

			// 4. Convert list to JSON String.
			result = mapper.writeValueAsString(resultLst);

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Filter comments using defined predicates.
	 * @param comment
	 * @param predicate
	 * @return
	 */
	private List<Comment> filterComments(List<Comment> comment, Predicate<Comment> predicate) {
		return comment.stream().filter(predicate).collect(Collectors.toList());
	}
}
