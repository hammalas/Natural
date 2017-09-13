/*
* generated by Xtext
*/
package org.agileware.natural.cucumber.ui.contentassist;

import java.util.Collection;

import org.agileware.natural.common.AbstractAnnotationDescriptor;
import org.agileware.natural.common.JavaAnnotationMatcher;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.impl.RuleCallImpl;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.google.inject.Inject;

/**
 * see
 * http://www.eclipse.org/Xtext/documentation/latest/xtext.html#contentAssist on
 * how to customize content assistant
 */
public class CucumberProposalProvider extends AbstractCucumberProposalProvider {

	@Inject
	private JavaAnnotationMatcher matcher;

	@Inject
	private AbstractAnnotationDescriptor descriptor;

	public void complete_Step(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (((RuleCallImpl) context.getLastCompleteNode().getGrammarElement()).getRule().getName().equals("EOL")
				) //&& context.getPrefix().length() == 0
			{
			for (String entry : descriptor.getNames()) {
				acceptor.accept(createCompletionProposal(entry + " ", context));
			}
		}
	}

	public void complete_StepDescription(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		Collection<String> proposals = matcher.findProposals();
		for (String proposal : proposals) {
			String display = proposal;
			if (proposal.charAt(0) == '^') {
				proposal = proposal.substring(1);
			}
			if (proposal.charAt(proposal.length() - 1) == '$') {
				proposal = proposal.substring(0, proposal.length() - 1);
			}
			acceptor.accept(createCompletionProposal(proposal, display, null, context));
		}
	}

	@Override
	public ICompletionProposal createCompletionProposal(String proposal, String displayString, Image image,
			ContentAssistContext context) {
		if (isValidProposal(proposal, context.getPrefix(), context)) {
			return doCreateProposal(proposal, new StyledString(displayString), image,
					getPriorityHelper().getDefaultPriority(), context);
		}
		return null;
	}

	@Override
	protected boolean isValidProposal(String proposal, String prefix, ContentAssistContext context) {
		if (proposal == null)
			return false;
		String[] words = prefix.split(" ");
		for (String word : words) {
			if (!proposal.toLowerCase().contains(word.toLowerCase()))
				return false;
		}
		if (getConflictHelper().existsConflict(proposal, context))
			return false;
		return true;
	}
}
