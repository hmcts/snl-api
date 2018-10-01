locals {
  app_full_name = "${var.product}-${var.component}"

  aat_events_url = "http://snl-events-aat.service.core-compute-aat.internal"
  local_events_url = "http://snl-events-${var.env}.service.${data.terraform_remote_state.core_apps_compute.ase_name[0]}.internal"
  events_url = "${var.env == "preview" ? local.aat_events_url : local.local_events_url}"
  events_keyvault = "${var.env == "preview" ? "events-aat" : var.env}"

}
module "snl-api" {
  source               = "git@github.com:hmcts/moj-module-webapp"
  product              = "${var.product}-${var.component}"
  location             = "${var.location}"
  env                  = "${var.env}"
  ilbIp                = "${var.ilbIp}"
  is_frontend          = false
  subscription         = "${var.subscription}"
  additional_host_name = "${var.external_host_name}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  common_tags          = "${var.common_tags}"

  app_settings = {
   SNL_EVENTS_URL = "${local.events_url}"
  }
}

data "azurerm_key_vault" "snl-events-vault" {
  name = "${local.events_keyvault}"
  resource_group_name = "${var.product}-aat"
}

data "azurerm_key_vault_secret" "snl-events-POSTGRES-USER" {
  name      = "${var.product}-events-POSTGRES-USER"
  vault_uri = "${data.azurerm_key_vault.snl-events-vault.vault_uri}"
}
