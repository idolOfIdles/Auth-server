import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

class AuthorizationFilter(authManager: AuthenticationManager?, var authJwtKey: String) : BasicAuthenticationFilter(authManager) {
    companion object{
        val TOKEN_HEADER = "Authorization"
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  chain: FilterChain) {
        val header = request.getHeader(AuthorizationFilter.TOKEN_HEADER)
        if (header != null) {
            SecurityContextHolder.getContext().authentication = authenticate(request)
        }
        chain.doFilter(request, response)
    }

    private fun authenticate(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(AuthorizationFilter.TOKEN_HEADER)
        val user = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(authJwtKey.toByteArray()))
                    .parseClaimsJws(token)
                    .body
       return UsernamePasswordAuthenticationToken(user, null, ArrayList())

    }
}
