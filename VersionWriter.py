# -*- coding: utf-8 -*-
"""
version.properties ファイルにバージョン情報を書き出します.

 gitのタグからmodのバージョン、
 MCPのcommandsからMCPのバージョンやクライアントのバージョンを取得しています。

 iron chest を色々パｋ…参考にさせて頂きました。
 https://github.com/cpw/ironchest

@author: alalwww
@version: 1.0.0

"""

import sys
import os
import commands
import fnmatch
import re
import subprocess
import shlex
import datetime

# constants
version_properties_filename = "version.properties"

cmd_describe = "git describe --long"
pattern_gittag = "v(\d+).(\d+)-(\d+)-(g.*)"
dummy_version_string = "v1.0-0-deadbeef"

pattern_mcp_commands_fullversion = "[.\w]+ \(data: ([.\w]+), client: ([.\w.]+), server: ([.\w.]+)\)"

# args
mcp_home = sys.argv[1]
mod_name = sys.argv[2]
fml_build_number = sys.argv[3]
mod_rev_number = sys.argv[4]

mcp_dir = os.path.abspath(mcp_home)
sys.path.append(mcp_dir)

from runtime.commands import Commands
Commands._version_config = os.path.join(mcp_dir, Commands._version_config)

def main():

    print("Obtaining version information from git")

    # get mod version
    try:
        process = subprocess.Popen(cmd_describe, bufsize= -1, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        git_tagstring, _ = process.communicate()
    except OSError as e:
        print("Git not found.")
        print("  type: " + str(type(e)))
        print("  " + str(e).decode("cp932").encode(encoding='utf-8'))
        print("----------")
        git_tagstring = dummy_version_string
    print("tag    : " + git_tagstring)
    print("pattern: " + pattern_gittag)
    (major, minor, build, githash) = re.match(pattern_gittag, git_tagstring).groups()

    # get mcp/minecraft version
    fullversion = Commands.fullversion()
    print("fullver: " + fullversion)
    print("pattern: " + pattern_mcp_commands_fullversion)
    (mcpversion, mcversion, mcserverversion) = re.match(pattern_mcp_commands_fullversion, fullversion).groups()

    # output
    with open(version_properties_filename, "w") as f:
      f.write("###################################################\n#\n")
      f.write("# version.properties\n")
      f.write("#\n")
      f.write("# create: " + str(datetime.datetime.now()) + "\n")
      f.write("#\n")
      f.write("###################################################\n")
      f.write("%s=%s\n" % (mod_name + ".version.major", major))
      f.write("%s=%s\n" % (mod_name + ".version.minor", minor))
      f.write("%s=%s\n" % (mod_name + ".version.build", build))
      f.write("%s=%s\n" % (mod_name + ".version.revision", mod_rev_number))
      f.write("%s=%s\n" % (mod_name + ".version.githash", githash))
      f.write("\n")
      f.write("%s=%s\n" % (mod_name + ".minecraft.version.client", mcversion))
      f.write("%s=%s\n" % (mod_name + ".minecraft.version.server", mcserverversion))
      f.write("%s=%s\n" % (mod_name + ".mcp.version", mcpversion))
      f.write("%s=%s\n" % (mod_name + ".fml.build.number", fml_build_number))

      f.write("#[EOF]")

    print("Version information: " + mod_name + " %s.%s.%s #%s using MCP %s for Minecraft %s" % (major, minor, build, mod_rev_number, mcpversion, mcversion))

if __name__ == '__main__':
    main()
