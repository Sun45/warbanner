import os
import json
from pathlib import Path

_MSG_TEMPLATE = """
{msg}
"""

def main():
  global _MSG_TEMPLATE
  with open("update_log.json", "r", encoding="utf8") as fp:
    logjson = json.load(fp)
  _MSG_TEMPLATE = _MSG_TEMPLATE.replace(
      "{msg}", logjson["content"])

  Path("releaselog_cache.txt").write_text(_MSG_TEMPLATE, "utf8")

  print(logjson["versionName"])

if __name__ == "__main__":
  main()