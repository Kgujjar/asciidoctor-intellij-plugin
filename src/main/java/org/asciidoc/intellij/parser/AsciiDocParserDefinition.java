package org.asciidoc.intellij.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.asciidoc.intellij.lexer.AsciiDocLexer;
import org.asciidoc.intellij.lexer.AsciiDocTokenTypes;
import org.asciidoc.intellij.psi.AsciiDocAttributeDeclarationImpl;
import org.asciidoc.intellij.psi.AsciiDocAttributeDeclarationName;
import org.asciidoc.intellij.psi.AsciiDocAttributeInBrackets;
import org.asciidoc.intellij.psi.AsciiDocAttributeReference;
import org.asciidoc.intellij.psi.AsciiDocBlockAttributes;
import org.asciidoc.intellij.psi.AsciiDocBlockIdImpl;
import org.asciidoc.intellij.psi.AsciiDocBlockMacro;
import org.asciidoc.intellij.psi.AsciiDocCell;
import org.asciidoc.intellij.psi.AsciiDocDescriptionItem;
import org.asciidoc.intellij.psi.AsciiDocDescriptionTerm;
import org.asciidoc.intellij.psi.AsciiDocFile;
import org.asciidoc.intellij.psi.AsciiDocHeading;
import org.asciidoc.intellij.psi.AsciiDocHtmlEntity;
import org.asciidoc.intellij.psi.AsciiDocIncludeTagInDocument;
import org.asciidoc.intellij.psi.AsciiDocInlineMacro;
import org.asciidoc.intellij.psi.AsciiDocLink;
import org.asciidoc.intellij.psi.AsciiDocList;
import org.asciidoc.intellij.psi.AsciiDocListItem;
import org.asciidoc.intellij.psi.AsciiDocRef;
import org.asciidoc.intellij.psi.AsciiDocSectionImpl;
import org.asciidoc.intellij.psi.AsciiDocStandardBlock;
import org.asciidoc.intellij.psi.AsciiDocTextQuoted;
import org.asciidoc.intellij.psi.AsciiDocTitle;
import org.asciidoc.intellij.psi.AsciiDocUrl;
import org.jetbrains.annotations.NotNull;

/**
 * @author yole
 */
public class AsciiDocParserDefinition implements ParserDefinition {
  // don't make these fields static, as the parser definition is a factory and will be created only once.
  // in the past, there have been NoClassDefFoundError - trying to avoid them by not having static attributes here.
  private final TokenSet whitespace = TokenSet.create(AsciiDocTokenTypes.LINE_BREAK, AsciiDocTokenTypes.EMPTY_LINE, AsciiDocTokenTypes.WHITE_SPACE, AsciiDocTokenTypes.WHITE_SPACE_MONO);
  private final TokenSet comments = TokenSet.create(AsciiDocTokenTypes.LINE_COMMENT, AsciiDocTokenTypes.BLOCK_COMMENT);

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new AsciiDocLexer();
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new AsciiDocParser();
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return AsciiDocElementTypes.FILE;
  }

  @NotNull
  @Override
  public TokenSet getWhitespaceTokens() {
    return whitespace;
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return comments;
  }

  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    return TokenSet.EMPTY;
  }

  @NotNull
  @Override
  public PsiElement createElement(ASTNode node) {
    if (node.getElementType() == AsciiDocElementTypes.SECTION) {
      return new AsciiDocSectionImpl(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.BLOCK_MACRO) {
      return new AsciiDocBlockMacro(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.INLINE_MACRO) {
      return new AsciiDocInlineMacro(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.BLOCK) {
      return new AsciiDocStandardBlock(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.DESCRIPTION_ITEM) {
      return new AsciiDocDescriptionItem(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.DESCRIPTION_TERM) {
      return new AsciiDocDescriptionTerm(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.LIST_ITEM) {
      return new AsciiDocListItem(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.LIST) {
      return new AsciiDocList(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.CELL) {
      return new AsciiDocCell(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.BLOCK_ATTRIBUTES) {
      return new AsciiDocBlockAttributes(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.ATTRIBUTE_IN_BRACKETS) {
      return new AsciiDocAttributeInBrackets(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.BLOCKID) {
      return new AsciiDocBlockIdImpl(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.REF) {
      return new AsciiDocRef(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.LINK) {
      return new AsciiDocLink(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.ATTRIBUTE_DECLARATION) {
      return new AsciiDocAttributeDeclarationImpl(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.ATTRIBUTE_REF) {
      return new AsciiDocAttributeReference(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.ATTRIBUTE_DECLARATION_NAME) {
      return new AsciiDocAttributeDeclarationName(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.URL) {
      return new AsciiDocUrl(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.TITLE) {
      return new AsciiDocTitle(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.QUOTED) {
      return new AsciiDocTextQuoted(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.HTML_ENTITY) {
      return new AsciiDocHtmlEntity(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.INCLUDE_TAG) {
      return new AsciiDocIncludeTagInDocument(node);
    }
    if (node.getElementType() == AsciiDocElementTypes.HEADING) {
      return new AsciiDocHeading(node);
    }
    throw new UnsupportedOperationException("Unknown node type " + node.getElementType());
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider fileViewProvider) {
    return new AsciiDocFile(fileViewProvider);
  }

  @Override
  public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode astNode, ASTNode astNode1) {
    return SpaceRequirements.MAY;
  }
}
