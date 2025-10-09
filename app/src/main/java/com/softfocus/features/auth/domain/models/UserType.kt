package com.softfocus.features.auth.domain.models

/**
 * Represents the different types of users in the Soft Focus platform.
 *
 * @property GENERAL Basic user without an assigned psychologist
 * @property PATIENT User with an assigned psychologist for therapy sessions
 * @property PSYCHOLOGIST Mental health professional providing therapy
 * @property ADMIN System administrator with full platform access
 */
enum class UserType {
    GENERAL,
    PATIENT,
    PSYCHOLOGIST,
    ADMIN
}