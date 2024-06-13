package com.example.data.data.model.account

import com.example.local.entity.account.UserAccountEntity
import com.example.remote.model.account.UserAccountRemoteModel
import com.example.remote.model.account.UserAccountSignInRemoteModel
import com.example.remote.model.account.UserAccountSignUpRemoteModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class UserAccount(
    val id: String = "",
    val name: Name = Name(),
    val email: String = "",
    val password: String = "",
    val photoUrl: String? = null,
    val createdAt: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
    val updatedAt: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
) {
    val fullName: String
        get() = "${name.first?.replaceFirstChar { it.uppercase() }} ${name.last?.replaceFirstChar { it.uppercase() }}"
}

data class Name(
    val first: String? = null,
    val last: String? = null
)

data class UserAccountSignUp(
    val name: Name = Name(),
    val email: String = "",
    val password: String = "",
)

data class UserAccountSignIn(
    val email: String = "",
    val password: String = "",
)

fun UserAccountSignUpRemoteModel.asUserAccountSignUp(): UserAccountSignUp =
    UserAccountSignUp(name = name.asName(), email, password)

fun UserAccountSignUp.asRemoteModel(): UserAccountSignUpRemoteModel =
    UserAccountSignUpRemoteModel(name.asRemoteName(), email, password)

fun UserAccount.asUserAccountSignUp(): UserAccountSignUp =
    UserAccountSignUp(name, email, password)


fun UserAccount.asUserAccountSignIn(): UserAccountSignIn =
    UserAccountSignIn(email, password)
//
//fun UserAccountSignUp.asUser(): UserAccount =
//    UserAccount(
//        name = name,
//        email = email,
//        password = password
//    )
//
//fun UserAccountSignIn.asUserAccount(): UserAccount =
//    UserAccount(
//        email = email,
//        password = password
//    )

fun UserAccountSignIn.asRemoteModel(): UserAccountSignInRemoteModel =
    UserAccountSignInRemoteModel(email, password)

fun UserAccount.asEntity(): UserAccountEntity =
    UserAccountEntity(
        id = id,
        firstName = name.first,
        lastName = name.last,
        email = email,
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun UserAccountEntity.asUserAccount(): UserAccount =
    UserAccount(
        id = id,
        name = Name(firstName, lastName),
        email = email,
        password = "",
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun UserAccountEntity.asRemoteModel(): UserAccountRemoteModel =
    UserAccountRemoteModel(
        id = id,
        name = Name(firstName, lastName).asRemoteName(),
        email = email,
        password = "",
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )


/* map to Entity */
fun UserAccountRemoteModel.asEntity(): UserAccountEntity =
    UserAccountEntity(
        id = id,
        firstName = name.first,
        lastName = name.last,
        email = email,
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun UserAccount.asRemoteModel(): UserAccountRemoteModel =
    UserAccountRemoteModel(
        id = id,
        name = name.asRemoteName(),
        email = email,
        password = password,
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun UserAccountRemoteModel.asUserAccount(): UserAccount =
    UserAccount(
        id = id,
        name = name.asName(),
        email = email,
        password = password,
        photoUrl = photoUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun Name.asRemoteName(): com.example.remote.model.account.Name =
    com.example.remote.model.account.Name(first, last)

fun com.example.remote.model.account.Name.asName(): Name = Name(first, last)