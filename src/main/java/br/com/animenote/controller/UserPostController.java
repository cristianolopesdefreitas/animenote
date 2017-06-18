package br.com.animenote.controller;

import java.util.Base64;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.animenote.constants.Status;
import br.com.animenote.constants.UserPostInteractions;
import br.com.animenote.model.Role;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionPost;
import br.com.animenote.model.UserPost;
import br.com.animenote.model.UserPostComment;
import br.com.animenote.model.UserPostCommentReport;
import br.com.animenote.model.UserPostReport;
import br.com.animenote.service.UserInteractionPostService;
import br.com.animenote.service.UserPostCommentReportService;
import br.com.animenote.service.UserPostCommentService;
import br.com.animenote.service.UserPostReportService;
import br.com.animenote.service.UserPostService;
import br.com.animenote.service.UserService;

@Controller
public class UserPostController {
	@Autowired
	private UserPostService userPostService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserPostCommentService userPostCommentService;

	@Autowired
	private UserPostReportService userPostReportService;

	@Autowired
	private UserPostCommentReportService userPostCommentReportService;

	@Autowired
	private UserInteractionPostService userInteractionPostService;
	
	private User getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		return userService.findByUsername(userDetails.getUsername());
	}
	
	private boolean isAdmin() {
		User user = getLoggedUser();
		
		for (Iterator<Role> iterator = user.getRoles().iterator(); iterator.hasNext();) {
			if ( "ADMIN".equals(iterator.next().getName()) ) {
				return true;
			}
		}
		
		return false;
	}

	@GetMapping("/postagem")
	public String writePost(UserPost userPost, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("userPost", userPost);

		return "post-registration";
	}

	@PostMapping("/postagem")
	public String savePost(@Valid UserPost userPost, BindingResult result, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		if (result.hasErrors()) {
			return this.writePost(userPost, model);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		userPost.setUser(user);
		userPost.setPostDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")));

		userPostService.savePost(userPost);

		return "redirect:/";
	}

	@GetMapping("/visualizar-postagem/{id}")
	public String viewPost(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		UserPost userPost = userPostService.findById(id);
		String avatar = null;
		
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());

		if (userPost != null) {
			if (userPost.getUser().getAvatar() != null) {
				avatar = "data:" + userPost.getUser().getAvatarType() + ";base64,"
						+ new String(Base64.getEncoder().encode(userPost.getUser().getAvatar()));
			}

			List<UserPostComment> postComments = userPostCommentService.findByUserPost(userPost);

			model.addAttribute("avatar", avatar);
			model.addAttribute("userPost", userPost);
			model.addAttribute("userPostComment", new UserPostComment());
			model.addAttribute("userPostComments", postComments);
			model.addAttribute("likeInteractions", userInteractionPostService
					.findByStatusAndUserPostInteractions(Status.A, UserPostInteractions.CURTIR).size());
			model.addAttribute("unlikeInteractions", userInteractionPostService
					.findByStatusAndUserPostInteractions(Status.A, UserPostInteractions.DESCURTIR).size());

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();

			User user = userService.findByUsername(userDetails.getUsername());

			model.addAttribute("userLoggedId", user.getId());
		} else {
			model.addAttribute("message", "Postagem não encontrada.");
		}

		return "post";
	}

	@PostMapping("/visualizar-postagem")
	public String savePostComment(@Valid UserPostComment userPostComment, @RequestParam("postId") Long postId,
			BindingResult result, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		if (result.hasErrors()) {
			return "redirect:/visualizar-postagem/" + postId;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		userPostComment.setUserPost(userPostService.findById(postId));
		userPostComment.setUser(user);
		userPostComment.setCommentDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")));

		userPostCommentService.savePostComment(userPostComment);

		return "redirect:/visualizar-postagem/" + postId;
	}

	@GetMapping("/excluir-postagem/{id}")
	public String deletePost(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		UserPost userPost = userPostService.findById(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		if (user.getId() == userPost.getUser().getId()) {
			userPostService.disablePost(userPost.getId());
			model.addAttribute("message", "Postagem apagada com sucesso!");
		} else {
			model.addAttribute("message", "Não é possível apagar esta postagem.");
		}

		return "deleted-post";
	}

	@GetMapping("/editar-postagem/{id}")
	public String editPost(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		UserPost userPost = userPostService.findById(id);

		if (userPost.getUser().getId() == user.getId()) {
			model.addAttribute("userPost", userPost);
		} else {
			model.addAttribute("userPost", new UserPost());
		}

		return "post-registration";
	}

	@GetMapping("/denunciar-postagem/{id}")
	public String viewReportPost(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("formAction", "/denunciar-postagem");
		return "report";
	}

	@GetMapping("/denunciar-postagem/{id}/{status}")
	public String viewReportPostResult(@PathVariable Long id, @PathVariable String status, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		if (status.equals("error")) {
			model.addAttribute("error", "Ocorreu um erro, não foi possível fazer a denúncia.");
		} else if (status.equals("success")) {
			model.addAttribute("success", "Sua denúncia foi feita com sucesso. Obrigado!");
		}

		model.addAttribute("formAction", "/denunciar-postagem");

		return "report";
	}

	@PostMapping("/denunciar-postagem")
	public String reportPost(@RequestParam("id") Long id, @RequestParam("report") String report, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		if (id == null || report.isEmpty()) {
			return "redirect:/denunciar-postagem/" + id + "/error";
		}

		UserPost userPost = userPostService.findById(id);

		if (userPost == null) {
			return "redirect:/denunciar-postagem/" + id + "/error";
		}

		UserPostReport userPostReport = new UserPostReport();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		userPostReport.setUserPostId(userPost);
		userPostReport.setReport(report);
		userPostReport.setUserReport(user);

		UserPostReport saved = userPostReportService.saveAndFlush(userPostReport);

		if (saved == null) {
			return "redirect:/denunciar-postagem/" + id + "/error";
		} else {
			return "redirect:/denunciar-postagem/" + id + "/success";
		}
	}

	@GetMapping("/denunciar-comentario/{id}")
	public String viewReportPostComment(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("formAction", "/denunciar-comentario");
		return "report";
	}

	@GetMapping("/denunciar-comentario/{id}/{status}")
	public String viewReportPostComment(@PathVariable Long id, @PathVariable String status, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		if (status.equals("error")) {
			model.addAttribute("error", "Ocorreu um erro, não foi possível fazer a denúncia.");
		} else if (status.equals("success")) {
			model.addAttribute("success", "Sua denúncia foi feita com sucesso. Obrigado!");
		}

		model.addAttribute("formAction", "/denunciar-comentario");

		return "report";
	}

	@PostMapping("/denunciar-comentario")
	public String reportPostComment(@RequestParam("id") Long id, @RequestParam("report") String report, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		if (id == null || report.isEmpty()) {
			return "redirect:/denunciar-comentario/" + id + "/error";
		}

		UserPostComment userPostComment = userPostCommentService.findById(id);

		if (userPostComment == null) {
			return "redirect:/denunciar-comentario" + id + "/error";
		}

		UserPostCommentReport userPostCommentReport = new UserPostCommentReport();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		userPostCommentReport.setUserPostCommentId(userPostComment);
		userPostCommentReport.setReport(report);
		userPostCommentReport.setUserReport(user);

		UserPostCommentReport saved = userPostCommentReportService.saveAndFlush(userPostCommentReport);

		if (saved == null) {
			return "redirect:/denunciar-comentario/" + id + "/error";
		} else {
			return "redirect:/denunciar-comentario/" + id + "/success";
		}
	}

	@GetMapping("/interagir-com-postagem/{id}/{interaction}")
	public String interactionPost(@PathVariable Long id, @PathVariable String interaction, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		UserPost userPost = userPostService.findById(id);

		if (userPost == null) {
			model.addAttribute("errorMessage", "Postagem não encontrada.");

			return "error-message";
		}
		
		if (userPost.getUser().getId() == user.getId()) {
			model.addAttribute("errorMessage", "Você não pode ter este tipo de interação com sua própria postagem.");

			return "error-message";
		}

		UserPostInteractions currentInteraction = null;

		if (interaction.equals("curtir")) {
			currentInteraction = UserPostInteractions.CURTIR;
		} else if (interaction.equals("descurtir")) {
			currentInteraction = UserPostInteractions.DESCURTIR;
		}

		UserInteractionPost userInteractionPost = userInteractionPostService.findByPostAndUser(userPost, user);

		if (userInteractionPost == null) {
			userInteractionPost = new UserInteractionPost();

			userInteractionPost.setPost(userPost);
			userInteractionPost.setUser(user);
			userInteractionPost.setStatus(Status.A);
		} else {
			if (userInteractionPost.getUserPostInteractions().equals(currentInteraction)) {
				if (userInteractionPost.getStatus().getValue().equals("Ativo")) {
					userInteractionPost.setStatus(Status.I);
				} else {
					userInteractionPost.setStatus(Status.A);
				}
			} else {
				userInteractionPost.setStatus(Status.A);
			}
		}
		
		userInteractionPost.setUserPostInteractions(currentInteraction);

		userInteractionPostService.saveAndFlush(userInteractionPost);

		return "redirect:/visualizar-postagem/" + id;
	}
}
