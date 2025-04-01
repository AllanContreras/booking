package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;

import java.util.List;

/**

 * Interface for managing user operations in the system.

 */

public interface UsersService {

    /**

     * Creates a user with administrator permissions.

     *

     * @param user  User model to be created.

     * @param role  Set of roles to assign to the user.

     * @return UserModel The created user object with updated details.

     * @throws AppException If an error occurs during user creation.

     */

    User createUserAsAdmin(User user, String role) throws AppException;

    /**

     * Creates a user with standard permissions.

     *

     * @param user User model to be created.

     * @return UserModel The created user object with updated details.

     * @throws AppException If an error occurs during user creation.

     */

    User createUserAsUser(User user) throws AppException;

    /**

     * Updates information for a specific user.

     *

     * @param id    Unique identifier of the user.

     * @param user  User model with new data.

     * @return UserModel The updated user object.

     * @throws AppException If an error occurs during user update.

     */

    User updateUser(String id, User user) throws AppException;

    /**

     * Retrieves a list of all users in the system.

     *

     * @return List<UserModel> List of user objects.

     * @throws AppException If an error occurs while retrieving the user list.

     */

    List<User> getAllUsers() throws AppException;

    /**

     * Retrieves a specific user by their identifier.

     *

     * @param id Unique identifier of the user.

     * @return UserModel The user object, if it exists.

     * @throws AppException If the user does not exist or an error occurs.

     */

    User getUserById(String id) throws AppException;

    /**

     * Deletes a specific user by their identifier.

     *

     * @param id Unique identifier of the user.

     * @return UserModel The deleted user object.

     * @throws AppException If the user does not exist or an error occurs during deletion.

     */

    User deleteUser(String id) throws AppException;

    /**

     * Authenticates a user based on their username and password.

     *

     * @param username Username for authentication.

     * @param password User's password.

     * @return UserModel The authenticated user object.

     * @throws AppException If credentials are invalid or an authentication error occurs.

     */

    User loginUser(String username, String password) throws AppException;

}
