package net.core.tools;


import net.core.db.interfaces.IUsersStorage;
import net.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("Authorization")
public class Authorization {

    @Autowired
    private IUsersStorage usersStorage;

    public boolean accessAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = usersStorage.getUsers(auth.getName());
        if (user.getRole().equals("ADMIN")) {
            return true;
        } else {
//            throw new ResourceNotFoundException();
            return false;
        }
    }
//    public ActionResult About() throws HttpException {
//        throw new ResourceNotFoundException("404");
//    }
}
