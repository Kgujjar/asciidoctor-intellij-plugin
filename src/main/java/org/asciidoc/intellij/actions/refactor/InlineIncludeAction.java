package org.asciidoc.intellij.actions.refactor;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import org.asciidoc.intellij.actions.asciidoc.AsciiDocAction;
import org.asciidoc.intellij.ui.InlineIncludeDialog;
import org.jetbrains.annotations.NotNull;

public class InlineIncludeAction extends AsciiDocAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    final PsiFile file = event.getData(LangDataKeys.PSI_FILE);
    final Project project = event.getProject();
    if (project == null || file == null) {
      return;
    }
    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    if (editor == null) {
      return;
    }

    PsiElement element = InlineIncludeDialog.getElement(editor, file);
    if (element == null) {
      return;
    }
    PsiNamedElement resolved = InlineIncludeDialog.resolve(element);
    if (resolved == null) {
      return;
    }

    final InlineIncludeDialog inlineIncludeDialog = new InlineIncludeDialog(project, element, resolved);
    inlineIncludeDialog.show();

  }

  public void update(AnActionEvent event) {
    PsiFile file = event.getData(LangDataKeys.PSI_FILE);
    final Project project = event.getProject();
    if (project == null || file == null) {
      return;
    }
    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    boolean enabled = false;

    PsiElement element = InlineIncludeDialog.getElement(editor, file);
    if (element != null) {
      PsiNamedElement resolved = InlineIncludeDialog.resolve(element);
      if (resolved != null) {
        enabled = true;
      }
    }

    event.getPresentation().setEnabledAndVisible(enabled);
  }

}
