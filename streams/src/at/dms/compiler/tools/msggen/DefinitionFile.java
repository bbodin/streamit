/*
 * Copyright (C) 1990-2001 DMS Decision Management Systems Ges.m.b.H.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id: DefinitionFile.java,v 1.4 2006-09-25 13:54:32 dimock Exp $
 */

package at.dms.compiler.tools.msggen;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import at.dms.compiler.tools.common.PositionedError;
import at.dms.compiler.tools.common.TokenReference;
import at.dms.compiler.tools.antlr.runtime.ParserException;
import at.dms.compiler.tools.common.Utils;

class DefinitionFile {

    /**
     * Constructs a token definition file
     */
    public DefinitionFile(String sourceFile,
                          String fileHeader,
                          String packageName,
                          String prefix,
                          String parent,
                          MessageDefinition[] definitions)
    {
        this.sourceFile = sourceFile;
        this.fileHeader = fileHeader;
        this.packageName    = packageName;
        this.prefix     = prefix == null ? DEFAULT_PREFIX : prefix;
        this.parent     = parent;
        this.definitions    = definitions;
    }

    /**
     * Reads and parses an token definition file
     *
     * @param   sourceFile      the name of the source file
     * @return  a class info structure holding the information from the source
     *
     */
    public static DefinitionFile read(String sourceFile) throws MsggenError {
        try {
            InputStream input = new BufferedInputStream(new FileInputStream(sourceFile));
            MsggenLexer scanner = new MsggenLexer(input);
            MsggenParser    parser = new MsggenParser(scanner);
            DefinitionFile  defs = parser.aCompilationUnit(sourceFile);

            input.close();

            return defs;
        } catch (FileNotFoundException e) {
            throw new MsggenError(MsggenMessages.FILE_NOT_FOUND, sourceFile);
        } catch (IOException e) {
            throw new MsggenError(MsggenMessages.IO_EXCEPTION, sourceFile, e.getMessage());
        } catch (ParserException e) {
            throw new MsggenError(MsggenMessages.FORMATTED_ERROR,
                                  new PositionedError(new TokenReference(sourceFile, e.getLine()),
                                                      MsggenMessages.SYNTAX_ERROR,
                                                      e.getMessage()));
        }
    }

    // --------------------------------------------------------------------
    // ACCESSORS
    // --------------------------------------------------------------------

    /**
     * Check for duplicate identifiers
     * @param   identifiers a table of all token identifiers
     * @param   prefix      the literal prefix
     * @param   id      the id of the first token
     * @return  the id of the last token + 1
     */
    public void checkIdentifiers(Hashtable<String, String> identifiers) throws MsggenError {
        for (int i = 0; i < definitions.length; i++) {
            definitions[i].checkIdentifiers(identifiers, sourceFile);
        }
    }

    /**
     * Prints the token definition to interface file (java)
     * @param   out     the output stream
     * @param   parent      the super interface
     */
    public void printFile(PrintWriter out) {
        if (fileHeader != null) {
            out.println(fileHeader);
        }
        out.print("// Generated by msggen from " + sourceFile);
        out.println();
        out.println("package " + packageName + ";");
        out.println();
        out.println("import at.dms.util.MessageDescription;");
        out.println();
        out.print("public interface " + prefix + "Messages");
        out.print(parent == null ? "" : " extends " + parent);
        out.println(" {");

        for (int i = 0; i < definitions.length; i++) {
            definitions[i].printInterface(out, this.prefix);
        }

        out.println("}");
    }

    /**
     * Returns the package name
     */
    public String getClassName() {
        return packageName + "." + prefix + "Messages";
    }

    /**
     * Returns the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Returns the literal prefix
     */
    public String getPrefix() {
        return prefix;
    }

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    private static final String     DEFAULT_PREFIX = "";

    private final String            sourceFile;
    private final String            fileHeader;
    private final String            packageName;
    private final String            prefix;
    private final String            parent;
    private final MessageDefinition[]   definitions;
}
