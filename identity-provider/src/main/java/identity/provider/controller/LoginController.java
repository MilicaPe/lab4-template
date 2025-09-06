package identity.provider.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@Controller
//public class LoginController {
//
//        @GetMapping("/login")
//        public String loginForm(HttpServletRequest request,
//                                @RequestParam(value = "error", required = false) String error,
//                                Model model) {
//
//            // Извлекаем параметры OAuth из запроса
//            String clientId = request.getParameter("client_id");
//            String redirectUri = request.getParameter("redirect_uri");
//            String state = request.getParameter("state");
//            String nonce = request.getParameter("nonce");
//            String scope = request.getParameter("scope");
//
//            model.addAttribute("scope", scope);
//
//
//            model.addAttribute("clientId", clientId);
//            model.addAttribute("redirectUri", redirectUri);
//            model.addAttribute("state", state);
//            model.addAttribute("nonce", nonce);
//            model.addAttribute("scope", scope);
//
//
//            if (error != null) {
//                model.addAttribute("error", "Invalid username or password");
//            }
//
//            return "login";
//        }
//
//
//    @PostMapping("/login")   // perform_
//    public ResponseEntity<?> performLogin() {
//        // Обработка логина выполняется Spring Security автоматически
//        return ResponseEntity.ok().build();
//    }
//
//}


/////
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request,
                            @RequestParam(value = "error", required = false) String error,
                            Model model) {

        // Сохраняем оригинальный запрос для redirect после успешного логина
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/api/v1/authorize")) {
            // Spring Security автоматически сохранит запрос через RequestCache
            // Не нужно делать это вручную
        }

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }

        return "login";
    }
}