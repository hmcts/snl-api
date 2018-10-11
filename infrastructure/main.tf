locals {
  app_full_name = "${var.product}-${var.component}"

  // Specifies the type of environment. var.env is replaced by pipline
  // to i.e. pr-102-snl so then we need just aat used here
  envInUse = "${(var.env == "preview" || var.env == "spreview") ? "aat" : var.env}"

  aat_events_url = "http://snl-events-aat.service.core-compute-aat.internal"
  local_events_url = "http://snl-events-${var.env}.service.${data.terraform_remote_state.core_apps_compute.ase_name[0]}.internal"
  events_url = "${var.env == "preview" ? local.aat_events_url : local.local_events_url}"

  // Shared Resources
  vaultName = "${var.raw_product}-${local.envInUse}"
  sharedResourceGroup = "${var.raw_product}-shared-infrastructure-${local.envInUse}"
}

module "snl-api" {
  source               = "git@github.com:hmcts/moj-module-webapp"
  product              = "${var.product}-${var.component}"
  location             = "${var.location}"
  env                  = "${var.env}"
  ilbIp                = "${var.ilbIp}"
  is_frontend          = "${var.external_host_name != "" ? "1" : "0"}"
  additional_host_name = "${var.external_host_name != "" ? var.external_host_name : "null"}"
  subscription         = "${var.subscription}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  common_tags          = "${var.common_tags}"

  app_settings = {
    SNL_EVENTS_URL = "${local.events_url}"
    SNL_S2S_JWT_SECRET = "${data.azurerm_key_vault_secret.s2s_jwt_secret.value}"
    SNL_FRONTEND_JWT_SECRET = "${data.azurerm_key_vault_secret.frontend_jwt_secret.value}"
  }
}

data "azurerm_key_vault" "snl-shared-vault" {
  name = "${local.vaultName}"
  resource_group_name = "${local.sharedResourceGroup}"
}

data "azurerm_key_vault_secret" "s2s_jwt_secret" {
  name      = "s2s-jwt-secret"
  vault_uri = "${data.azurerm_key_vault.snl-shared-vault.vault_uri}"
}

data "azurerm_key_vault_secret" "frontend_jwt_secret" {
  name      = "frontend-jwt-secret"
  vault_uri = "${data.azurerm_key_vault.snl-shared-vault.vault_uri}"
}
